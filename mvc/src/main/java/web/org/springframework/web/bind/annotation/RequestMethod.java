package web.org.springframework.web.bind.annotation;

import java.util.Arrays;
import web.org.springframework.web.bind.exception.NotAllowedMethodException;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod findRequestMethod(final String methodName) {
        return Arrays.stream(values())
                .filter(value -> value.name().equals(methodName))
                .findAny()
                .orElseThrow(NotAllowedMethodException::new);
    }
}
