package com.interface21.webmvc.servlet.mvc.tobe.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerCreationException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(ControllerCreationException.class);

    public ControllerCreationException(String message) {
        super(message);
        log.error("Failed to create Controller instance : {}", message, this);
    }
}
