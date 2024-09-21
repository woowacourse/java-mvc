package com.interface21.web.bind.annotation;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod of(String method) {
        return Arrays.stream(values())
                .filter(requestMethod -> method.equals(requestMethod.name()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("지원하지 않는 메소드(%s)입니다.".formatted(method)));
    }
}
