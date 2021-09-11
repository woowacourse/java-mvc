package nextstep.web.support;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod find(String methodName) {
        return Arrays.stream(RequestMethod.values())
                .filter(method -> method.name().equalsIgnoreCase(methodName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 메소드가 없어요"));
    }
}
