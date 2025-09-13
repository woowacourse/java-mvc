package com.interface21.web.bind.annotation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    private static final Map<String, RequestMethod> lookup = new HashMap<>();

    static {
        for (RequestMethod method : values()) {
            lookup.put(method.name(), method);
        }
    }

    public static Optional<RequestMethod> from(final String name) {
        if (name == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(lookup.get(name.toUpperCase()));
    }
}
