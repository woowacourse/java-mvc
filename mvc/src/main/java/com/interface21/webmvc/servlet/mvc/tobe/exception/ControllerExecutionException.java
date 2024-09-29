package com.interface21.webmvc.servlet.mvc.tobe.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerExecutionException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(ControllerExecutionException.class);

    public ControllerExecutionException(String message, Throwable e) {
        super(message);
        log.error("ControllerExecutionException: {}: {}", e.getClass().getName(), message, e);
    }
}
