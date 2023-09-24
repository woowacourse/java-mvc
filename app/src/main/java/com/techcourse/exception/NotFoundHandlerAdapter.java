package com.techcourse.exception;

public class NotFoundHandlerAdapter extends IllegalArgumentException {

    public NotFoundHandlerAdapter(final String message) {
        super(message);
    }
}
