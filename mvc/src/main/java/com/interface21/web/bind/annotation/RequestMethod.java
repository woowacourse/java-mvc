package com.interface21.web.bind.annotation;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod from(final String method) {
        return Arrays.stream(values())
            .filter(value -> value.name().equalsIgnoreCase(method))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 HTTP Method 입니다. " + method));
    }
}
