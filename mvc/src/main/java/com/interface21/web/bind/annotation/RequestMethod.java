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

    public static RequestMethod findByName(String name) {
        return Arrays.stream(RequestMethod.values())
                .filter(method -> method.name().equals(name))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(name + "에 해당하는 HTTP method가 없습니다."));
    }
}
