package com.interface21.web.bind.annotation;

import java.util.Arrays;

public enum RequestMethod {

    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE,
    ;

    public static RequestMethod of(final String name) {
        if (name == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(method -> method.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
