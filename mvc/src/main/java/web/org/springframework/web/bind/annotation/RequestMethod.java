package web.org.springframework.web.bind.annotation;

import java.util.Arrays;

public enum RequestMethod {
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE;

    public static RequestMethod from(final String methodName) {
        return Arrays.stream(values())
                     .filter(requestMethod -> requestMethod.name().equalsIgnoreCase(methodName))
                     .findAny()
                     .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 HTTP Method 입니다."));
    }
}
