package com.interface21.web.bind.annotation;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod from(String name) {
        return Arrays.stream(values())
                .filter(method -> method.name().equalsIgnoreCase(name))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
