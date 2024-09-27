package com.interface21.webmvc.servlet.exception;

public class NotFoundHandlerAdapterException extends RuntimeException {
    private static final String ERROR_MESSAGE = "해당 핸들러를 처리할 수 없습니다.";

    public NotFoundHandlerAdapterException() {
        super(ERROR_MESSAGE);
    }
}
