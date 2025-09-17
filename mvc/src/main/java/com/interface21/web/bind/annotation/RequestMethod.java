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

    public static RequestMethod from(String requestMethodText) {
        return Arrays.stream(values())
            .filter(method -> method.name().equals(requestMethodText))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 요청 메서드입니다."));
    }
}
