package nextstep.web.support;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod match(String method) {
        return Arrays.stream(RequestMethod.values())
                .filter(value -> value.name().equals(method))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("일치하는 http 메소드가 없습니다."));
    }
}
