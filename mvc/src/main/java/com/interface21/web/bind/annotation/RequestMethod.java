package com.interface21.web.bind.annotation;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod find(String raw) {
        return Arrays.stream(values())
                .filter(value -> value.name().equals(raw))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("유효하지 않은 요청 메서드입니다: %s", raw)));
    }
}
