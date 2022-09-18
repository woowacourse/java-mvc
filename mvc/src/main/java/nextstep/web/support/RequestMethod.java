package nextstep.web.support;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod from(String requestMethod) {
        return Arrays.stream(values())
                .filter(value -> value.name().equals(requestMethod))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
