package com.interface21.webmvc.servlet.mvc.exception;

public class HandlerNotFoundException extends RuntimeException {

    public HandlerNotFoundException(final String message) {
        super(message);
    }
}
