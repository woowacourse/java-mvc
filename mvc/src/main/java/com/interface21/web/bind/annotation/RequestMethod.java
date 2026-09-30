package com.interface21.web.bind.annotation;

import java.util.stream.Stream;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod resolve(final String method) {
        return Stream.of(values())
                .filter(m -> m.name().equalsIgnoreCase(method))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 메소드입니다"));
    }
}
