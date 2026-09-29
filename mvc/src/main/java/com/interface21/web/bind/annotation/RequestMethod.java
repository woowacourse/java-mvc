package com.interface21.web.bind.annotation;

import java.util.stream.Stream;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod resolve(final String method) {
        return Stream.of(values())
                .filter(m -> m.name().equalsIgnoreCase(method))
                .findFirst()
                .orElse(null);
    }
}
