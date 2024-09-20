package com.interface21.webmvc.servlet.mvc.tobe.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerMappingException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(HandlerMappingException.class);

    public HandlerMappingException(String message) {
        super(message);
        log.error("Failed to create controller instance : {}", message, this);
    }
}
