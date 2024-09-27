package com.interface21.webmvc.servlet;

public class NotFoundHandlerAdapter extends RuntimeException {
    public NotFoundHandlerAdapter(String message) {
        super(message);
    }
}
