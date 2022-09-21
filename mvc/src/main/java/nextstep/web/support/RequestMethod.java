package nextstep.web.support;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod from(String method) {
        return Arrays.stream(values())
                .filter(requestMethod -> method.equalsIgnoreCase(requestMethod.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("일치하는 HTTP Method가 없습니다. :" + method));
    }
}
