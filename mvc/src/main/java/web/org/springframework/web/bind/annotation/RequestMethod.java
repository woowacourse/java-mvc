package web.org.springframework.web.bind.annotation;

import java.util.Arrays;

public enum RequestMethod {
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    CONNECT,
    OPTIONS,
    TRACE,
    ;

    public static RequestMethod find(final String methodName) {
        return Arrays.stream(RequestMethod.values())
                .filter(method -> method.name().equalsIgnoreCase(methodName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 HTTP 요청 메서드입니다"));
    }
}
