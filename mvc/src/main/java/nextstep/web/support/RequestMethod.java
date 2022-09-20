package nextstep.web.support;

import java.util.Arrays;
import java.util.NoSuchElementException;

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

    public static RequestMethod from(String httpMethod) {
        return Arrays.stream(values())
                .filter(requestMethod -> requestMethod.name().equalsIgnoreCase(httpMethod))
                .findAny()
                .orElseThrow(NoSuchElementException::new);
    }
}
