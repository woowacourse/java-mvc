package com.interface21.webmvc.servlet.exception;

public class NotFoundController extends RuntimeException {

    public NotFoundController(final String message) {
        super(message);
    }
}
