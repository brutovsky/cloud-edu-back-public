package com.nakytniak.common.api.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.nakytniak.common.api.client.exception.HttpClientException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.util.StreamUtils;
import org.springframework.web.server.ServerErrorException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
public class HttpClient {

    protected final HttpTransport httpTransport = new NetHttpTransport();

    private final ObjectMapper objectMapper;
    private final String mediaType;
    protected final int connectionTimeout;
    protected final int readTimeout;

    public HttpClient(final ObjectMapper objectMapper, final MediaType mediaType, final int connectionTimeout,
            final int readTimeout) {
        this.objectMapper = objectMapper.copy();
        this.mediaType = mediaType.getType();
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
    }

    public <T> T performRequest(final HttpRequest request, final TypeReference<T> responseType) {
        try {
            final HttpResponse httpResponse = request.execute();
            final String responseContent = getResponseContent(httpResponse.getContent());
            if (httpResponse.isSuccessStatusCode() && !responseContent.equals("")) {
                log.info("Received response with status: [{}]", httpResponse.getStatusCode());
                log.debug("Response body: [{}]", responseContent);
                return objectMapper.readValue(responseContent, responseType);
            } else {
                log.warn("Unable to perform request: {}: {}.\nStatus: [{}], Response: [{}]",
                        request.getRequestMethod(), request.getUrl(), httpResponse.getStatusCode(), responseContent);
                throw new ServerErrorException(httpResponse.getStatusMessage(), (Throwable) null);
            }
        } catch (IOException e) {
            log.warn("Unable to perform request: [{}].", request.getUrl(), e);
            throw new HttpClientException(String.format("Unable to perform request: %s.", request.getUrl()), e);
        }
    }

    private String getResponseContent(final InputStream responseBody) throws IOException {
        return StreamUtils.copyToString(responseBody, Charset.defaultCharset());
    }

    @SneakyThrows
    public <T> HttpRequest createRequest(final String url, final HttpMethod method, @Nullable final T body)
            throws IOException {
        final HttpContent content = getContent(url, method, body);

        return httpTransport
                .createRequestFactory()
                .buildRequest(method.name(), new GenericUrl(url), content)
                .setConnectTimeout(connectionTimeout)
                .setReadTimeout(readTimeout)
                .setWriteTimeout(readTimeout);
    }

    @SneakyThrows
    public <T> HttpRequest createRequestWithAuthorization(final String url, final HttpMethod method, final String token,
            @Nullable final T body) {
        final HttpContent content = getContent(url, method, body);
        final HttpHeaders headers = new HttpHeaders();
        headers.setAuthorization(String.format("Bearer %s", token));

        return httpTransport
                .createRequestFactory()
                .buildRequest(method.name(), new GenericUrl(url), content)
                .setConnectTimeout(connectionTimeout)
                .setReadTimeout(readTimeout)
                .setWriteTimeout(readTimeout)
                .setHeaders(headers);
    }

    private <T> ByteArrayContent getContent(final String url, final HttpMethod method, @Nullable final T body) {
        return Optional.ofNullable(body)
                .map(bodyEntity -> {
                    try {
                        final String requestBody = objectMapper.writeValueAsString(body);
                        log.info("Sending request: \nMethod: [{}] \nUrl: [{}]", url, method.name());
                        log.debug("Request body: [{}]", requestBody);
                        return requestBody;
                    } catch (JsonProcessingException e) {
                        throw new IllegalArgumentException(String.format("Unable to parse object: %s.%nError: %s",
                                body.getClass(), e.getMessage()));
                    }
                })
                .map(contentString -> new ByteArrayContent(mediaType, contentString.getBytes(StandardCharsets.UTF_8)))
                .orElseGet(() -> new ByteArrayContent(mediaType, new byte[]{}));
    }

}
