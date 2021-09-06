package nextstep.web.support;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod of(String methodName){
        return Arrays.stream(values())
            .filter(it -> it.toString().equalsIgnoreCase(methodName))
            .findAny()
            .orElseThrow(IllegalArgumentException::new);
    }

}
