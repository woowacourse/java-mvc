package com.interface21.webmvc.servlet.mvc.tobe.exception;

public class ControllerResultCastException extends RuntimeException {

    public ControllerResultCastException(String methodName) {
        super(String.format("Controller method returned an unsupported type : %s", methodName));
    }
}
