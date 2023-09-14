package web.org.springframework.web.bind.annotation;

import web.org.springframework.exception.BindException;

import java.util.Arrays;
import java.util.Locale;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod from(final String value) {
        return Arrays.stream(RequestMethod.values())
                .filter(requestMethod -> requestMethod.name().equals(value.toUpperCase(Locale.ENGLISH)))
                .findFirst()
                .orElseThrow(() -> new BindException("[ERROR] 입력받은 값으로 RequestMethod 를 찾을 수 없는 오류가 발생하였습니다."));
    }
}
