package com.interface21.web.bind.annotation;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public enum RequestMethod {
    GET, POST, PUT, DELETE, PATCH, OPTIONS, HEAD, TRACE;

    private static final Map<String, RequestMethod> cache;

    static {
        cache = Arrays.stream(values())
                .collect(Collectors.toMap(RequestMethod::name, e -> e));
    }

    public static RequestMethod from(String methodName) {
        return Optional.ofNullable(cache.get(methodName.toUpperCase()))
                .orElseThrow(() -> new IllegalArgumentException(methodName + "은(는) 올바른 HTTP 메서드가 아닙니다."));
    }
}
