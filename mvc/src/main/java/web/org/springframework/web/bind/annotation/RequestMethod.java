package web.org.springframework.web.bind.annotation;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod from(String methodName) {
        String upperMethodName = methodName.toUpperCase();
        return Arrays.stream(values())
                .filter(requestMethod -> requestMethod.name().equals(upperMethodName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메서드 입니다."));
    }
}
