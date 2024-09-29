package com.interface21.webmvc.servlet.mvc.tobe.exception;

public class HandlerAdapterNotFoundException extends RuntimeException {

    public HandlerAdapterNotFoundException() {
        super("No HandlerAdapter found for handler");
    }
}
