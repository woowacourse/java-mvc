package com.techcourse;

public class CommonMethodInvokeException extends RuntimeException {
    public CommonMethodInvokeException(final String message, final Exception cause) {
        super(message, cause);
    }
}
