package com.nakytniak.service;

import com.google.cloud.storage.BlobId;
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

    private static final int SIGNED_URL_DURATION = 15;
    public static final String FILE_PATH_TEMPLATE = "gs://%s/%s";
    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".csv", ".json"};

    public String uploadFile(final InputStream fileInputStream, final String bucketName, final String fileName) {
        final boolean extensionAllowed = checkFileExtension(fileName);

        if (!extensionAllowed) {
            throw new IllegalArgumentException(String.format("Wrong file extension [%s], expected one of: %s",
                    fileName.substring(fileName.lastIndexOf('.')), Arrays.toString(ALLOWED_EXTENSIONS)));
        }

        final BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName).build();
        @SuppressWarnings("deprecation") final BlobInfo newBlobInfo = storage.create(blobInfo, fileInputStream);
        logger.log(Level.INFO, "Uploaded file {0} as {1}", new Object[]{fileName, newBlobInfo.getName()});

        return String.format(FILE_PATH_TEMPLATE, bucketName, newBlobInfo.getName());
    }

    public URL generateSignedUrl(final String bucketName, final String objectName) {
        final BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, objectName).build();
        try (Storage storage = storageProvider.createStorageService()) {
            if (Objects.isNull(storage.get(blobInfo.getBlobId()))) {
                throw new EntityNotFoundException(
                        String.format("Storage object not found for blobInfo: %s", blobInfo.getBlobId()));
            }
            return storage.signUrl(blobInfo, SIGNED_URL_DURATION, TimeUnit.MINUTES, SignUrlOption.withV4Signature());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteFile(final String bucketName, final String filePath) {
        final BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, filePath).build();
        final BlobId blobId = blobInfo.getBlobId();
        if (Objects.isNull(storage.get(blobId))) {
            throw new EntityNotFoundException(String.format("Storage object not found for blobInfo: %s", blobId));
        }
        return storage.delete(blobId);
    }

    private boolean checkFileExtension(final String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return Arrays.stream(ALLOWED_EXTENSIONS).anyMatch(fileName::endsWith);
        } else {
            return false;
        }
    }
}
