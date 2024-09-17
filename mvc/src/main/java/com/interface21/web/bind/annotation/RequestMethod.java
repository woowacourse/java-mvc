package com.interface21.web.bind.annotation;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum RequestMethod {
    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE");

    private static final Map<String, RequestMethod> SUIT_CASE = Arrays.stream(RequestMethod.values())
            .collect(Collectors.toMap(RequestMethod::getValue, Function.identity()));

    private final String value;

    RequestMethod(String value) {
        this.value = value;
    }

    public static RequestMethod getByValue(String value) {
        if (SUIT_CASE.containsKey(value)) {
            return SUIT_CASE.get(value);
        }
        throw new IllegalArgumentException(String.format("%s는 지원하지 않는 메서드입니다.", value));
    }

    private String getValue() {
        return value;
    }
}
