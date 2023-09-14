package web.org.springframework.web.bind.annotation;

import web.org.springframework.web.exception.RequestMethodNotFoundException;

import java.util.Arrays;

public enum RequestMethod {
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE,
    ;

    public static RequestMethod from(String requestMethod) {
        return Arrays.stream(values())
                     .filter(it -> it.name().equals(requestMethod))
                     .findFirst()
                     .orElseThrow(() -> new RequestMethodNotFoundException("지원하지 않는 요청 방식입니다."));
    }
}
