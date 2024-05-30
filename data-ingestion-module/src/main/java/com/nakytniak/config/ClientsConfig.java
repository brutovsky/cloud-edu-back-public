package com.nakytniak.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nakytniak.common.api.client.HttpClient;
import com.nakytniak.common.api.client.SecuredGoogleHttpClient;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;


@RequiredArgsConstructor
@Configuration
public class ClientsConfig {

    @Value("${iap.client.id}")
    private String iapClientId;
    @Value("${http.client.connection.timeout}")
    private int connectionTimeout;
    @Value("${http.client.read.timeout}")
    private int readTimeout;

    private final ObjectMapper objectMapper;

    @Bean
    public SecuredGoogleHttpClient httpClient() {
        return new SecuredGoogleHttpClient(objectMapper.copy(), MediaType.APPLICATION_JSON, iapClientId, connectionTimeout,
                readTimeout);
    }

}
