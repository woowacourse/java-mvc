package nextstep.web.support;

import java.util.Arrays;
import nextstep.web.support.exception.RequestMethodNotFoundException;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod from(final String method) {
        return Arrays.stream(values())
                .filter(requestMethod -> method.equalsIgnoreCase(requestMethod.name()))
                .findFirst()
                .orElseThrow(RequestMethodNotFoundException::new);
    }
}
