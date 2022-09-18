package nextstep.web.support;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod find(final String method) {
        return Arrays.stream(values())
                .filter(value -> value.name().equals(method))
                .findAny()
                .get();
    }
}
