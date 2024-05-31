package com.nakytniak.exception;

public class EntityAlreadyExistsException extends RuntimeException {

    public EntityAlreadyExistsException(final String message) {
        super(message);
    }

    public EntityAlreadyExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
