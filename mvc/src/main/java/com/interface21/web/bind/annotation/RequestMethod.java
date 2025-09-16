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

    public static RequestMethod from(final String requestMethod) {
        return Arrays.stream(RequestMethod.values())
                .filter(value -> value.name().equals(requestMethod))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하는 HTTP 메서드가 없습니다."));
    }
}
