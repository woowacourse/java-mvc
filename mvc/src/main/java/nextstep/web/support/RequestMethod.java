package nextstep.web.support;

import java.util.Arrays;

public enum RequestMethod {
    GET("get"),
    HEAD("head"),
    POST("post"),
    PUT("put"),
    PATCH("patch"),
    DELETE("delete"),
    OPTIONS("options"),
    TRACE("trace");

    private final String value;

    RequestMethod(final String value) {
        this.value = value;
    }

    public static RequestMethod from(final String requestMethod) {
        return Arrays.stream(RequestMethod.values())
                .filter(it -> it.value.equalsIgnoreCase(requestMethod))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
