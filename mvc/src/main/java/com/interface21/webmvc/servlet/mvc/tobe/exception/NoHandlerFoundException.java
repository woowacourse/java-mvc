package com.interface21.webmvc.servlet.mvc.tobe.exception;

public class NoHandlerFoundException extends RuntimeException {

    public NoHandlerFoundException(String path) {
        super("No handler found for path: " + path);
    }
}
