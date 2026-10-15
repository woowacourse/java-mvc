package com.interface21.webmvc.servlet.mvc.tobe;

public class HandlerNotFoundException extends RuntimeException {

    public HandlerNotFoundException(final String method, final String requestURI) {
        super("요청을 처리할 핸들러가 없습니다: " + method + " " + requestURI);
    }
}
