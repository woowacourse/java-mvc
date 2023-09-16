package com.techcourse;

public class HandlerMethodNotFoundException extends RuntimeException {

    public HandlerMethodNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
