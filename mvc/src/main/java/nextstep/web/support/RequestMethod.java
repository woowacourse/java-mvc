package nextstep.web.support;

import java.util.Arrays;
import nextstep.mvc.common.exception.ErrorType;
import nextstep.mvc.common.exception.InvalidRequestMethodException;

public enum RequestMethod {

    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE,
    ;

    public static RequestMethod find(final String name) {
        return Arrays.stream(values())
                .filter(method -> name.equalsIgnoreCase(method.name()))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestMethodException(ErrorType.INVALID_REQUEST_METHOD));
    }
}
