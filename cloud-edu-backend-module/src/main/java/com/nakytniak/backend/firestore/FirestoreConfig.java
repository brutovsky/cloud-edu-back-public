package com.nakytniak.backend.firestore;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class FirestoreConfig {

    @Value("${gcp.projectId}")
    private String projectId;

    @Value("${spring.cloud.gcp.firestore.database-ids}")
    private String[] databaseIds;

    @Bean
    public Map<String, Firestore> firestoreMap() throws IOException {
        Map<String, Firestore> firestoreMap = new HashMap<>();

        for (String databaseId : databaseIds) {
            FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
                    .setProjectId(projectId)
                    .setDatabaseId(databaseId)
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .build();
            Firestore firestore = firestoreOptions.getService();
            firestoreMap.put(databaseId, firestore);
        }

        return firestoreMap;
    }

    @Bean
    @Primary
    public Firestore defaultFirestore(final Map<String, Firestore> firestoreMap) {
        return firestoreMap.values().iterator().next();
    }

}
