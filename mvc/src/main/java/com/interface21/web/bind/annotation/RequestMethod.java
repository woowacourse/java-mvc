package com.interface21.web.bind.annotation;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod of(final String name) {
        try {
            return RequestMethod.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
