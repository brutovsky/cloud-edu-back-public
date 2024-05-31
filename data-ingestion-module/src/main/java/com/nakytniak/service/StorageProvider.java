package com.nakytniak.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ImpersonatedCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Service
public class StorageProvider {
    private static final String CLOUD_PLATFORM_SCOPE = "https://www.googleapis.com/auth/cloud-platform";
    private static final Integer CREDENTIALS_LIFE_TYPE_SECONDS = 30;

    @Value("${storage.blob.signer.sa}")
    private String urlSignerServiceAccount;

    @Value("${gcp.projectId}")
    private String projectId;

    public Storage createStorageService() {
        final var cred = getAccountCredentials();
        try {
            cred.refresh();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return StorageOptions.newBuilder()
                .setCredentials(cred)
                .setProjectId(projectId).build()
                .getService();
    }

    @SneakyThrows
    private GoogleCredentials getAccountCredentials() {
        return ImpersonatedCredentials
                .create(
                        GoogleCredentials.getApplicationDefault(),
                        urlSignerServiceAccount,
                        null,
                        Collections.singletonList(CLOUD_PLATFORM_SCOPE),
                        CREDENTIALS_LIFE_TYPE_SECONDS
                );
    }
}
