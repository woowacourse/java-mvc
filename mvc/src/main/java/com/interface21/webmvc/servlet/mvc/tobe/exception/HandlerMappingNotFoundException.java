package com.interface21.webmvc.servlet.mvc.tobe.exception;

public class HandlerMappingNotFoundException extends RuntimeException {

    public HandlerMappingNotFoundException() {
        super("No HandlerMapping found for request");
    }
}
