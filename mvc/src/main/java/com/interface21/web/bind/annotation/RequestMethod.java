package com.interface21.web.bind.annotation;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    private static final RequestMethod[] ALL = RequestMethod.values();
    private static final Map<String, RequestMethod> CLASSIFY =
            Arrays.stream(RequestMethod.values())
                    .collect(Collectors.toMap(RequestMethod::name, Function.identity()));

    public static RequestMethod from(final String method) {
        return Optional.ofNullable(CLASSIFY.get(method.toUpperCase()))
                .orElseThrow(() -> new NotSupportMethodException(String.format("%s 는 지원하지 않는 HTTP 메소드입니다.", method)));
    }

    public static RequestMethod[] all() {
        return ALL.clone();
    }
}
