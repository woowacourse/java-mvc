package com.interface21.webmvc.servlet.mvc.tobe;

public class HandlerCreationException extends RuntimeException {

    public HandlerCreationException(String handlerName, Throwable cause) {
        super("Error creating handler with name '%s'".formatted(handlerName), cause);
    }
}
