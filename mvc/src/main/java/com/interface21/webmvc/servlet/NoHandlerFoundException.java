package com.interface21.webmvc.servlet;

public class NoHandlerFoundException extends RuntimeException {

    public NoHandlerFoundException(String message) {
        super(message);
    }
}
