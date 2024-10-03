package com.interface21.webmvc.servlet.mvc.tobe.exception;

public class ControllerCreationException extends RuntimeException {

    public ControllerCreationException(String message, Throwable e) {
        super(String.format("Failed to create Controller instance [%s, %s]", e.getClass().getName(), message), e);
    }
}
