package nextstep.web.support;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod find(final String requestMethod) {

        return Arrays.stream(values())
                .filter(it -> it.name().equals(requestMethod))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 HTTP 메서드입니다."));


    }
}
