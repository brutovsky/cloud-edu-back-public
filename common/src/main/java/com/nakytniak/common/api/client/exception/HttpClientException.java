package com.nakytniak.common.api.client.exception;

public class HttpClientException extends RuntimeException {
    public HttpClientException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
