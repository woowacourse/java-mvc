package com.interface21.web.bind.annotation;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    private static final Map<String, RequestMethod> CONVERTER = Stream.of(RequestMethod.values())
            .collect(Collectors.toMap(Enum::name, method -> method));

    public static RequestMethod from(final String name) {
        if (!CONVERTER.containsKey(name)) {
            throw new IllegalArgumentException("존재하지 않은 요청 메서드 이름");
        }
        return CONVERTER.get(name);
    }
}
