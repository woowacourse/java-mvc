package com.interface21.webmvc.servlet.mvc;

public class NoHandlerFoundException extends Exception {
    public NoHandlerFoundException(String httpMethod, String requestURL) {
        super("처리할 수 없는 엔드포인트입니다 : " + httpMethod + " " + requestURL);
    }
}
