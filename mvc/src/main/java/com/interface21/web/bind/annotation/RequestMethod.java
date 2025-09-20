package com.interface21.web.bind.annotation;

import java.util.Arrays;

public enum RequestMethod {

    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE");

    private final String name;

    RequestMethod(String name) {
        this.name = name;
    }

    public static RequestMethod fromString(String name) {
        return Arrays.stream(values())
                .filter(method -> method.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
