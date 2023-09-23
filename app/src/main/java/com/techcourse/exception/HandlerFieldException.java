package com.techcourse.exception;

public class HandlerFieldException extends RuntimeException {

    public HandlerFieldException() {
        super("Handler needs Field name 'controllers'");
    }
}
