package nextstep.web.support;

import java.util.Arrays;
import java.util.function.Predicate;
import nextstep.web.exception.support.RequestMethodException;

public enum RequestMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    HEAD("HEAD"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE");

    private final String type;

    RequestMethod(String type) {
        this.type = type;
    }

    public static RequestMethod from(String value) {
        return Arrays.stream(RequestMethod.values())
            .filter(isType(value))
            .findFirst()
            .orElseThrow(RequestMethodException::new);
    }

    private static Predicate<RequestMethod> isType(String value) {
        return type -> type.getType().equals(value);
    }

    public String getType() {
        return type;
    }
}
