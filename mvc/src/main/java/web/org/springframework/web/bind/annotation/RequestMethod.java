package web.org.springframework.web.bind.annotation;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE
    ;

    public static RequestMethod from(final String request) {
        return Arrays.stream(RequestMethod.values())
                .filter(method -> method.name().equalsIgnoreCase(request))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not supported request method: " + request));
    }
}
