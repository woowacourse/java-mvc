package com.interface21.webmvc.servlet.mvc.tobe.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnprocessableHandlerException extends RuntimeException{

    private static final Logger log = LoggerFactory.getLogger(UnprocessableHandlerException.class);

    public UnprocessableHandlerException(String handlerClassName) {
        super("처리할 수 없는 유형의 Handler 입니다.");
        log.error("Failed to process handler of type: {}", handlerClassName, this);
    }
}
