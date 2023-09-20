package com.techcourse.exception;

public class HandlerAdapterNotFoundException extends DispatcherServletException {
    public HandlerAdapterNotFoundException(final String message) {
        super(message);
    }
}
