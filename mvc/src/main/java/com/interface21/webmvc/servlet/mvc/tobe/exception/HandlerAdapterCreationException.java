package com.interface21.webmvc.servlet.mvc.tobe.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerAdapterCreationException extends  RuntimeException{

    private static final Logger log = LoggerFactory.getLogger(HandlerAdapterCreationException.class);

    public HandlerAdapterCreationException(String message, Throwable e) {
        super(message, e);
        log.error("HandlerAdapterCreationException: {}: {}", e.getClass().getName(), message, e);
    }
}
