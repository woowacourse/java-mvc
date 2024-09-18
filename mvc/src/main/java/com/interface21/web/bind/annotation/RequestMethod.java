package com.interface21.web.bind.annotation;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod from(String method) {
        return Arrays.stream(values())
                .filter(value -> value.isMethodMatch(method))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("일치하는 Request Method가 없습니다. 입력된 method = " + method));
    }

    private boolean isMethodMatch(String method) {
        return this.name().equals(method);
    }
}
