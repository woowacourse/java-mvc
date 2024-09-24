package com.interface21.webmvc.servlet.view;

public class NotSupportViewException extends RuntimeException {
    public NotSupportViewException(final String message, final Exception e) {
        super(message,e);
    }
}
