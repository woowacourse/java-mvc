package nextstep.web.support;

import java.util.Arrays;

import nextstep.mvc.exception.RequestMethodException;

public enum RequestMethod {
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE;

    public static RequestMethod from(String input) {
        return Arrays.stream(values())
            .filter(method -> method.name().equals(input))
            .findFirst()
            .orElseThrow(() -> new RequestMethodException("적절한 Request Method를 찾을 수 없습니다."));
    }
}
