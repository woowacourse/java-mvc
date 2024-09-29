package com.interface21.webmvc.servlet;

public class NotFoundHandlerException extends RuntimeException {

    public NotFoundHandlerException() {
        super("요청을 처리할 수 있는 핸들러가 존재하지 않습니다.");
    }
}
