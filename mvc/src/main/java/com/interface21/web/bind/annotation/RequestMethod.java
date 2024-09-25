package com.interface21.web.bind.annotation;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod findMethod(String method) {
        return Arrays.stream(RequestMethod.values())
                .filter(requestMethod -> requestMethod.name().equalsIgnoreCase(method))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 RequestMethod 입니다."));
    }
}
