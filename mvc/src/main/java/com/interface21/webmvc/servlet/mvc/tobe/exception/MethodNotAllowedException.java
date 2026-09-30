package com.interface21.webmvc.servlet.mvc.tobe.exception;

public class MethodNotAllowedException extends RuntimeException {
    public MethodNotAllowedException(final String methodName) {
        super("Cannot resolve method: " + methodName);
    }
}
