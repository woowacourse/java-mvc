package web.org.springframework.web.bind.annotation;

import java.util.Arrays;

public enum RequestMethod {
    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE");

    private final String methodName;

    RequestMethod(String methodName) {
        this.methodName = methodName;
    }

    public static RequestMethod from(String methodName) {
        return Arrays.stream(values())
                .filter(method -> method.methodName.equals(methodName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메소드입니다"));
    }
}
