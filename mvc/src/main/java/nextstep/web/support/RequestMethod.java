package nextstep.web.support;

import java.util.Arrays;

public enum RequestMethod {

    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod find(final String method) {
        return Arrays.stream(values())
                .filter(value -> value.name().equals(method))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("지원되지 않는 HTTP 메서드입니다."));
    }
}
