package com.interface21.web.bind.annotation;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod find(String requestMethod) {
        return Arrays.stream(RequestMethod.values())
            .filter(method -> method.name().equalsIgnoreCase(requestMethod))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("Invalid request method: " + requestMethod));
    }
}
