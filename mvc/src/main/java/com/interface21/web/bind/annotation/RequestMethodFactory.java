package com.interface21.web.bind.annotation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RequestMethodFactory {

    private static final Map<String, RequestMethod> lookup = new HashMap<>();

    static {
        for (final var method : RequestMethod.values()) {
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
