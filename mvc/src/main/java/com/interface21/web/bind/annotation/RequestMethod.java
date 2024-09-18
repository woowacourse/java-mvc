package com.interface21.web.bind.annotation;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod from(String method) {
        return Arrays.stream(RequestMethod.values())
                .filter(value -> value.name().equals(method.toUpperCase()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 RequestMethod를 찾을 수 없습니다"));
    }
}
