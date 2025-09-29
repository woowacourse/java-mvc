package com.interface21.webmvc.servlet.mvc.tobe.exception;

public class HandlerNotFoundException extends RuntimeException {

    public HandlerNotFoundException(final String message) {
        super(message);
    }
}
