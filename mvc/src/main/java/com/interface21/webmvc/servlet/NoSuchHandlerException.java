package com.interface21.webmvc.servlet;

import jakarta.servlet.http.HttpServletRequest;

public class NoSuchHandlerException extends RuntimeException {

    public NoSuchHandlerException(HttpServletRequest request) {
        super(String.format("%s %s 에 대한 적합한 핸들러가 존재하지 않습니다.", request.getRequestURI(), request.getMethod()));
    }
}
