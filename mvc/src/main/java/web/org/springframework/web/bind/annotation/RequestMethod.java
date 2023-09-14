package web.org.springframework.web.bind.annotation;

import web.org.springframework.web.bind.exception.InvalidRequestMethodException;

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

    public static RequestMethod from(final String requestMethod) {
        return Arrays.stream(values())
                     .filter(method -> method.name().equalsIgnoreCase(requestMethod))
                     .findAny()
                     .orElseThrow(InvalidRequestMethodException::new);
    }
}
