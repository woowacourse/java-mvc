package com.interface21.web.bind.annotation;

import java.util.Arrays;

public enum RequestMethod {

    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod from(String rawRequestMethod) {
        return Arrays.stream(values())
                .filter(method -> method.name().equals(rawRequestMethod.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 HTTP 메서드입니다."));
    }
}
