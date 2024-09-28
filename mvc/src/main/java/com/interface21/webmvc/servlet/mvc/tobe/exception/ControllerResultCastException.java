package com.interface21.webmvc.servlet.mvc.tobe.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerResultCastException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(ControllerResultCastException.class);

    public ControllerResultCastException(String methodName) {
        super("Controller method returned an unsupported type");
        log.error("ControllerResultCastException: {}", methodName, this);
    }
}
