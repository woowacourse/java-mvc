package nextstep.web.support;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod from(String methodName) {
        return Arrays.stream(values())
                .filter(requestMethod -> requestMethod.isSame(methodName))
                .findAny()
                .orElseThrow(() -> new RuntimeException("매칭되는 메서드가 없습니다"));
    }

    public boolean isSame(String methodName) {
        return name().equalsIgnoreCase(methodName);
    }
}
