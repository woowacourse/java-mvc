package com.interface21.web.bind.annotation;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod of(String method) {
        return Arrays.stream(RequestMethod.values())
                .filter(m -> m.toString().equals(method))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 메서드입니다."));
    }
}
