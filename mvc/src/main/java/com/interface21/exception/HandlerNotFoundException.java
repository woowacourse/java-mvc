package com.interface21.exception;

import jakarta.servlet.http.HttpServletRequest;

public class HandlerNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE_FORMAT = "처리할 수 있는 핸들러가 존재하지 않습니다 [method = %s, uri = %s]";

    public HandlerNotFoundException(HttpServletRequest request) {
        super(ERROR_MESSAGE_FORMAT.formatted(request.getMethod(), request.getRequestURI()));
    }
}
