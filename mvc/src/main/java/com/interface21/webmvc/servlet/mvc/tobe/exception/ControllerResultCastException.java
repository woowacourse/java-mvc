package com.interface21.webmvc.servlet.mvc.tobe.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerResultCastException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(ControllerResultCastException.class);

    public ControllerResultCastException(String methodName) {
        super("Controller의 메서드 반환 결과가 처리할 수 없는 유형입니다.");
        log.error("Failed to cast result in handler method: {}", methodName, this);
    }
}
