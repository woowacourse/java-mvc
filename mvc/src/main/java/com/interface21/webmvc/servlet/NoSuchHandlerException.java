package com.interface21.webmvc.servlet;

public class NoSuchHandlerException extends RuntimeException {

    public NoSuchHandlerException() {
        super("적합한 핸들러가 존재하지 않습니다.");
    }
}
