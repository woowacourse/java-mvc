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
    TRACE,
    ;

    public static RequestMethod from(final String httpMethod) {
        return Arrays.stream(values())
                     .filter(requestMethod -> requestMethod.name().equals(httpMethod))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("Invalid HTTP Method"));
    }
}
