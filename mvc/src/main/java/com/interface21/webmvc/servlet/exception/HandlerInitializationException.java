package com.interface21.webmvc.servlet.exception;

public class HandlerInitializationException extends RuntimeException {

    public HandlerInitializationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
