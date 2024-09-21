package com.interface21.web.bind.annotation;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod from(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("유효한 Http Method 가 아닙니다.");
        }
    }
}
