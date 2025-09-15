package com.interface21.web.bind.annotation;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE, NONE;

    public static RequestMethod from(final String method) {
        return Arrays.stream(values())
                .filter(requestMethod -> requestMethod.name().equals(method))
                .findFirst()
                .orElse(NONE);
    }
}
