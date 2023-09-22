package com.techcourse.exception;

public class HandlerNotFoundException extends RuntimeException {

    public HandlerNotFoundException(final String message) {
        super(message);
    }
}
