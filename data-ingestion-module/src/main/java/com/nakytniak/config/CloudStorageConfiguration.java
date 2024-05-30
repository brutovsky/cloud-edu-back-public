package com.nakytniak.config;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudStorageConfiguration {

    @Value("${gcp.projectId}")
    private String projectId;

    @Bean
    public Storage createStorageService() {
        return StorageOptions.newBuilder().setProjectId(projectId).build().getService();
    }

    @Bean
    BigQuery createBigQueryService() {
        return BigQueryOptions.getDefaultInstance().getService();
    }

}
