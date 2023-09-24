package com.techcourse.exception;

public class NotFoundHandlerMapping extends IllegalArgumentException {

    public NotFoundHandlerMapping(final String message) {
        super(message);
    }
}
