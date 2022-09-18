package nextstep.web.support;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum RequestMethod {

    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE"),
    ;

    private final String value;

    RequestMethod(final String value) {
        this.value = value;
    }

    public static RequestMethod from(final String method) {
        return Arrays.stream(values())
                .filter(requestMethod -> requestMethod.isSame(method))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당하는 HttpMethod가 존재하지 않습니다."));
    }

    private boolean isSame(final String method) {
        return this.value.equals(method);
    }

    public String getValue() {
        return value;
    }
}
