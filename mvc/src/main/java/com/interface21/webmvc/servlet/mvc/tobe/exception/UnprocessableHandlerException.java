package com.interface21.webmvc.servlet.mvc.tobe.exception;

public class UnprocessableHandlerException extends RuntimeException {

    public UnprocessableHandlerException(String handlerClassName) {
        super(String.format("The handler is of an unsupported type : %s", handlerClassName));
    }
}
