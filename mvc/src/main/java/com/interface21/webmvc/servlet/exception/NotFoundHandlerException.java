package com.interface21.webmvc.servlet.exception;

public class NotFoundHandlerException extends RuntimeException {
    private static final String ERROR_MESSAGE = "해당 요청에 해당하는 핸들러가 없습니다.";

    public NotFoundHandlerException() {
        super(ERROR_MESSAGE);
    }
}
