package nextstep.web.support;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod from(String methodName){
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(methodName))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
