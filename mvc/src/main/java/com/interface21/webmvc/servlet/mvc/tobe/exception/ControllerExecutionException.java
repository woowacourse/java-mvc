package com.interface21.webmvc.servlet.mvc.tobe.exception;

public class ControllerExecutionException extends RuntimeException {

    public ControllerExecutionException(String message, Throwable e) {
        super(String.format("An error occurred while executing the Controller [%s, %s]", e.getClass().getName(),
                message), e);
    }
}
