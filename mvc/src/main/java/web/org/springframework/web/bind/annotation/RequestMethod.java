package web.org.springframework.web.bind.annotation;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod from(final String method) {
        return Arrays.stream(values())
                .filter(requestMethod -> requestMethod.name().equalsIgnoreCase(method))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Method 입니다."));
    }
}
