package com.interface21.web.bind.annotation;

import java.util.Arrays;
import java.util.Objects;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod from(String method) {
        return Arrays.stream(values())
                .filter(requestMethod -> Objects.equals(requestMethod.name(), method))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Method [" + method + "] is not supported"));
    }
}
