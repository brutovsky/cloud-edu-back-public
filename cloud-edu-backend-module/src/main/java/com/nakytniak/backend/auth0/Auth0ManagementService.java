package com.nakytniak.backend.auth0;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.api.client.http.HttpRequest;
import com.nakytniak.backend.auth0.dto.Auth0UserDto;
import com.nakytniak.backend.auth0.dto.CreateAuth0User;
import com.nakytniak.common.api.client.HttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.nakytniak.backend.auth0.utils.PasswordGenerator.generateRandomPassword;

@RequiredArgsConstructor
@Service
public class Auth0ManagementService {

    @Value("${auth0.api.management.token}")
    private String accessToken;

    @Value("${okta.oauth2.issuer}")
    private String oauth2TenantUrl;

    private final HttpClient httpClient;

    private static final String ENDPOINT_URL = "%s/api/v2/users";

    public String createUser() {
        return createUser("email@example.com");
    }

    public String createUser(final String email) {
        final CreateAuth0User userToCreate = CreateAuth0User.builder()
                .email(email)
                .name("student")
                .password(generateRandomPassword(12))
                .verifyEmail(true)
                .emailVerified(false)
                .connection("Username-Password-Authentication")
                .build();
        final HttpRequest httpRequest = httpClient.createRequestWithAuthorization(getAuth0EndpointUrl(),
                HttpMethod.POST,
                accessToken, userToCreate);
        return httpClient.performRequest(httpRequest, new TypeReference<>() {
        });
    }

    public List<Auth0UserDto> getUsers() {
        final HttpRequest httpRequest = httpClient.createRequestWithAuthorization(getAuth0EndpointUrl(), HttpMethod.GET,
                accessToken, null);
        return httpClient.performRequest(httpRequest, new TypeReference<>() {
        });
    }

    private String getAuth0EndpointUrl() {
        return String.format(ENDPOINT_URL, oauth2TenantUrl);
    }

}
