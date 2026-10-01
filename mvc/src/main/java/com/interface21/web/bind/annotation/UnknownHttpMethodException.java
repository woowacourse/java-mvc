package com.interface21.web.bind.annotation;

public class UnknownHttpMethodException extends RuntimeException {

    public UnknownHttpMethodException(final String method) {
        super("지원하지 않는 HTTP 메서드: " + method);
    }
}
