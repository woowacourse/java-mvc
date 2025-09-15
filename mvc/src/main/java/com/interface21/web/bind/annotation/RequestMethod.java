package com.interface21.web.bind.annotation;

import java.util.Locale;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;


    public static RequestMethod from(String method) {
        if (method == null || method.isBlank()) {
            throw new IllegalArgumentException("HTTP method must not be null or empty");
        }

        return RequestMethod.valueOf(method.toUpperCase(Locale.ROOT));
    }
}
