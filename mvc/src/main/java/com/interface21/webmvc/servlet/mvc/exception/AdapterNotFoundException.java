package com.interface21.webmvc.servlet.mvc.exception;

public class AdapterNotFoundException extends RuntimeException {
    public AdapterNotFoundException(Object handler) {
        super("No handler adapter found for " + handler.getClass().getName());
    }
}
