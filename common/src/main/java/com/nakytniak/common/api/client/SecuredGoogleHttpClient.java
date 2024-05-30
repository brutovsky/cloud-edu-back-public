package com.nakytniak.common.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.util.Preconditions;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.IdTokenCredentials;
import com.google.auth.oauth2.IdTokenProvider;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Collections;

@Slf4j
public class SecuredGoogleHttpClient extends HttpClient {

    private static final String IAM_SCOPE = "https://www.googleapis.com/auth/iam";

    private final String iapClientId;

    public SecuredGoogleHttpClient(final ObjectMapper objectMapper, final MediaType mediaType,
            final String iapClientId, final int connectionTimeout,
            final int readTimeout) {
        super(objectMapper, mediaType, connectionTimeout, readTimeout);
        this.iapClientId = iapClientId;
    }

    @Override
    public <T> HttpRequest createRequest(final String url, final HttpMethod method, final T body) throws IOException {
        return populateRequestWithIapToken(super.createRequest(url, method, body));
    }

    private HttpRequest populateRequestWithIapToken(final HttpRequest request) throws IOException {
        final IdTokenProvider idTokenProvider = getIdTokenProvider();
        final IdTokenCredentials credentials =
                IdTokenCredentials.newBuilder()
                        .setIdTokenProvider(idTokenProvider)
                        .setTargetAudience(iapClientId)
                        .build();

        credentials.refresh();
        log.debug("IapClientId: {}", iapClientId);
        log.debug("IdTokenProvider: {}", idTokenProvider);
        log.debug("Obtained ID token: {}", credentials.getIdToken().getTokenValue());

        final HttpRequestInitializer httpRequestInitializer = new HttpCredentialsAdapter(credentials);

        HttpRequest newRequest = httpTransport
                .createRequestFactory(httpRequestInitializer)
                .buildRequest(request.getRequestMethod(), request.getUrl(), request.getContent());

        newRequest.setConnectTimeout(connectionTimeout);
        newRequest.setReadTimeout(readTimeout);
        newRequest.setWriteTimeout(readTimeout);

        log.debug("Request headers: {}", newRequest.getHeaders());

        return newRequest;
    }

    @SuppressFBWarnings("BC_UNCONFIRMED_CAST_OF_RETURN_VALUE")
    private IdTokenProvider getIdTokenProvider() throws IOException {
        final GoogleCredentials credentials =
                GoogleCredentials.getApplicationDefault().createScoped(Collections.singleton(IAM_SCOPE));

        Preconditions.checkNotNull(credentials, "Expected to load credentials");
        Preconditions.checkState(
                credentials instanceof IdTokenProvider,
                String.format(
                        "Expected credentials that can provide ID tokens, got %s instead",
                        credentials.getClass().getName()));

        return (IdTokenProvider) credentials;
    }
}
