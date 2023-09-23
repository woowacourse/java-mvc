package com.techcourse.exception;

public class HandlerNotFoundException extends DispatcherServletException {
    public HandlerNotFoundException(final String message) {
        super(message);
    }
}
