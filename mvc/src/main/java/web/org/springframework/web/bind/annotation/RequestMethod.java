package web.org.springframework.web.bind.annotation;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod getRequestMethod(String method) {
        return Arrays.stream(values())
                .filter(value -> value.name().equalsIgnoreCase(method))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 요청입니다"));
    }
}
