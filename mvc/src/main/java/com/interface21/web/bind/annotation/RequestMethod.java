package com.interface21.web.bind.annotation;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod of(final String httpMethod) {
        return Arrays.stream(values())
                .filter(rm -> rm.name().equalsIgnoreCase(httpMethod))
                .findAny()
                .orElseThrow();
    }
}
