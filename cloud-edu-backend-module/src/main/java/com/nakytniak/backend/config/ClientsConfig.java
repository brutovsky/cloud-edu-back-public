package com.nakytniak.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nakytniak.common.api.client.HttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

@RequiredArgsConstructor
@Configuration
public class ClientsConfig {

    @Value("${http.client.connection.timeout}")
    private int connectionTimeout;
    @Value("${http.client.read.timeout}")
    private int readTimeout;

    private final ObjectMapper objectMapper;

    @Bean
    public HttpClient httpClient() {
        return new HttpClient(objectMapper, MediaType.APPLICATION_JSON, connectionTimeout, readTimeout);
    }

}
