package com.interface21.web.bind.annotation;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod findByName(String name) {
        return Arrays.stream(values())
                .filter(o -> o.name().equals(name))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("not valid requestMethod"));
    }
}
