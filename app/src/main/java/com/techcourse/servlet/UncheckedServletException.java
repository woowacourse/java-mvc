package com.techcourse.servlet;

public class UncheckedServletException extends RuntimeException {

    public UncheckedServletException(Exception e) {
        super(e);
    }
}
