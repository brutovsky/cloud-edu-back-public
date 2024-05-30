package com.nakytniak.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.SignUrlOption;
import com.nakytniak.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class CloudStorageService {

    private final Logger logger = Logger.getLogger(CloudStorageService.class.getName());

    private final Storage storage;

    private final StorageProvider storageProvider;

    public static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".csv"};

    public String uploadFile(final InputStream fileInputStream, final String bucketName, final String fileName) {
        final boolean extensionAllowed = checkFileExtension(fileName);

        if (!extensionAllowed) {
            throw new IllegalArgumentException(String.format("Wrong file extension [%s], expected one of: %s",
                    fileName.substring(fileName.lastIndexOf('.')), Arrays.toString(ALLOWED_EXTENSIONS)));
        }

        @SuppressWarnings("deprecation") final BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, fileName).build(), fileInputStream);
        logger.log(Level.INFO, "Uploaded file {0} as {1}", new Object[]{fileName, blobInfo.getName()});

        return blobInfo.getName();
    }

    public URL generateSignedUrl(final String bucketName, final String objectName) {
        final BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, objectName).build();
        final Storage storage = storageProvider.createStorageService();
        if (Objects.isNull(storage.get(blobInfo.getBlobId()))) {
            throw new EntityNotFoundException(
                    String.format("Storage object not found for blobInfo: %s", blobInfo.getBlobId()));
        }
        return storage.signUrl(blobInfo, 15, TimeUnit.MINUTES, SignUrlOption.withV4Signature());
    }

    private boolean checkFileExtension(final String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return Arrays.stream(ALLOWED_EXTENSIONS).anyMatch(fileName::endsWith);
        } else {
            return false;
        }
    }
}
