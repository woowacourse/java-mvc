package com.interface21.webmvc.servlet.mvc.tobe.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerCreationException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(ControllerCreationException.class);

    public ControllerCreationException(String message, Throwable e) {
        super(message, e);
        log.error("ControllerCreationException: {}: {}", e.getClass().getName(), message, e);
    }
}
