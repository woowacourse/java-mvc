package nextstep.web.support;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod requestMethod(String method) {
        return Arrays.stream(values())
                .filter(value -> value.name().equals(method))
                .findAny()
                .orElseThrow(() -> new RuntimeException("존재하지 않은 Method 입니다."))
                ;
    }
}
