package com.interface21.web.support;

public class NotFoundMethodArgumentResolver extends RuntimeException {
    public NotFoundMethodArgumentResolver(String message) {
        super(message);
    }
}
