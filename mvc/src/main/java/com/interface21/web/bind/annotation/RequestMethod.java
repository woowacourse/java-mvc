package com.interface21.web.bind.annotation;

import java.util.Arrays;
import java.util.Optional;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static Optional<RequestMethod> fromHttpMethod(String httpMethod) {
        return Arrays.stream(values())
                .filter(requestMethod -> requestMethod.name().equals(httpMethod))
                .findFirst();
    }
}
