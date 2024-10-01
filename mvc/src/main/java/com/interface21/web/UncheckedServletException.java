package com.interface21.web;

public class UncheckedServletException extends RuntimeException {

    public UncheckedServletException(Exception e) {
        super(e);
    }
}
