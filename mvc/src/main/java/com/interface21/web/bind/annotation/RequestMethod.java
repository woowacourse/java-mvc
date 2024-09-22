package com.interface21.web.bind.annotation;

import java.util.stream.Stream;

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

    public static RequestMethod from(String method) {
        return Stream.of(values())
                .filter(requestMethod -> requestMethod.name().equals(method))
                .findAny()
                .orElseThrow(() -> new NoSuchMethodError(method + "를 찾을 수 없습니다."));
    }
}
