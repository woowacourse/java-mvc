package com.interface21.webmvc.servlet;

public class NoHandlerFoundException extends RuntimeException {

    public NoHandlerFoundException(final String httpMethod, final String requestURI) {
        super(httpMethod + " " + requestURI + "에 해당하는 핸들러를 찾을 수 없습니다");
    }

}
