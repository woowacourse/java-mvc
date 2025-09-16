package com.interface21.web.bind.annotation;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod from(final String method) {
        return Arrays.stream(values())
                .filter(requestMethod -> requestMethod.name().equals(method))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 http 메서드입니다: " + method));
    }
}
