package com.interface21.webmvc.servlet.exception;

public class HandlerExecutionException extends RuntimeException {
    private static final String ERROR_MESSAGE = "컨트롤러 반환 값을 처리할 수 없습니다";

    public HandlerExecutionException() {
        super(ERROR_MESSAGE);
    }
}
