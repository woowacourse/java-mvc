package web.org.springframework.web.bind.annotation;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod from(String method) {
        return Arrays.stream(values())
                .filter(value -> value.name().equalsIgnoreCase(method))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 메소드입니다. method: " + method));
    }
}
