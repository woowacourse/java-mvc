package com.interface21.webmvc.servlet.mvc.tobe.exception;

public class HandlerAdapterCreationException extends RuntimeException {

    public HandlerAdapterCreationException(String message, Throwable e) {
        super(String.format("Failed to create HandlerAdapter [%s, %s]", e.getClass().getName(), message), e);
    }
}
