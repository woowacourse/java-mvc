package com.techcourse.servlet;

public class UncheckedServletException extends RuntimeException {

    public UncheckedServletException(final Exception e) {
        super(e);
    }
}
