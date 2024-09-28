package com.interface21.webmvc.servlet.mvc.tobe.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnprocessableHandlerException extends RuntimeException{

    private static final Logger log = LoggerFactory.getLogger(UnprocessableHandlerException.class);

    public UnprocessableHandlerException(String handlerClassName) {
        super("The handler is of an unsupported type");
        log.error("UnprocessableHandlerException: {}", handlerClassName, this);
    }
}
