package web.org.springframework.web.bind.annotation;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Objects;

public enum RequestMethod {
    GET("get"),
    HEAD("head"),
    POST("post"),
    PUT("put"),
    PATCH("patch"),
    DELETE("delete"),
    OPTIONS("options"),
    TRACE("trace"),
    ;

    private final String value;

    RequestMethod(final String value) {
        this.value = value;
    }

    public boolean isMatching(final HttpServletRequest request) {
        final String method = request.getMethod();
        return this.value.equalsIgnoreCase(method);
    }
}
