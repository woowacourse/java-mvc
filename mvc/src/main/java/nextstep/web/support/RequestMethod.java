package nextstep.web.support;

import java.util.Arrays;
import nextstep.web.exception.MethodNotAllowedException;

public enum RequestMethod {
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE;

    public static RequestMethod find(String method) {
        return Arrays.stream(values())
                .filter(value -> value.name().equals(method))
                .findAny()
                .orElseThrow(MethodNotAllowedException::new);
    }
}

