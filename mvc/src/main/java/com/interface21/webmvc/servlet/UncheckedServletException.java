package com.interface21.webmvc.servlet;

public class UncheckedServletException extends RuntimeException {

    public UncheckedServletException(Exception e) {
        super(e);
    }
}
