package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;

public class NoMatchedHandlerException extends RuntimeException {
    public NoMatchedHandlerException(String message) {
        super(message);
    }

    public NoMatchedHandlerException(HttpServletRequest request) {
        this(String.format("%s %s 를 처리할 핸들러가 존재하지 않습니다.",
                request.getRequestURI(), request.getMethod()));
    }
}
