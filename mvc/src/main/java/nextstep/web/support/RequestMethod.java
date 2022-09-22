package nextstep.web.support;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod of(final String method) {
        return Arrays.stream(values())
                .filter(requestMethod -> requestMethod.name().equalsIgnoreCase(method))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 HTTP 메서드입니다."));
    }
}
