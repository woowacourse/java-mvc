package com.interface21.webmvc.servlet.mvc.tobe.exception;

public class HandlerMappingCreationException extends RuntimeException {

    public HandlerMappingCreationException(String message, Throwable e) {
        super(String.format("Failed to create HandlerMapping [%s, %s]", e.getClass().getName(), message), e);
    }
}
