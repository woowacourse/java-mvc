package com.interface21.web.bind.annotation;

import java.util.Locale;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod getBy(final String name) {
        try {
            return valueOf(name.toUpperCase(Locale.ROOT).trim());
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported HTTP method.");
        }
    }
}
