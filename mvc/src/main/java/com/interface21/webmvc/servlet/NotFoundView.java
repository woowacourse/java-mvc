package com.interface21.webmvc.servlet;

public class NotFoundView extends RuntimeException {
    public NotFoundView(String message) {
        super(message);
    }
}
