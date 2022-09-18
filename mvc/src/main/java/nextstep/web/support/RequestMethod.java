package nextstep.web.support;

import java.util.Arrays;

public enum RequestMethod {

    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE
    ;

    public static RequestMethod from(final String value) {
        return Arrays.stream(values())
            .filter(method -> method.name().equals(value.toUpperCase()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(String.format("정의되지 않은 메서드입니다. [%s]", value)));
    }
}
