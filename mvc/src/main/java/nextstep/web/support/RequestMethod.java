package nextstep.web.support;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;


    public static RequestMethod of(String method) {
        return Arrays.stream(values())
                .filter(requestMethod -> requestMethod.name().equals(method))
                .findFirst()
                .orElseThrow();
    }
}
