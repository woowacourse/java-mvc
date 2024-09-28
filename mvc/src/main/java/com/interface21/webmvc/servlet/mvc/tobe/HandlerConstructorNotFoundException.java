package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;

public class HandlerConstructorNotFoundException extends RuntimeException {

    public HandlerConstructorNotFoundException(Method method, Throwable cause) {
        super(getMessage(method), cause);
    }

    private static String getMessage(Method method) {
        return String.format("메서드가 선언된 핸들러의 생성자를 찾을 수 없습니다. - method: %s", method.getName());
    }
}
