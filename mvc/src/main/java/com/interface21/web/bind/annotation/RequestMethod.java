package com.interface21.web.bind.annotation;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE
    ;
    public static RequestMethod findByName(String name){
        return Arrays.stream(RequestMethod.values())
                .filter(value -> value.name().equals(name))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("일치하는 HTTP 메서드가 없습니다."));
    }
}
