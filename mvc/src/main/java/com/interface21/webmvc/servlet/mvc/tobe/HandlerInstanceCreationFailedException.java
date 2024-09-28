package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;

public class HandlerInstanceCreationFailedException extends RuntimeException {

    public HandlerInstanceCreationFailedException(Method method, Throwable cause) {
        super(getMessage(method), cause);
    }

    private static String getMessage(Method method) {
        return String.format("메서드가 선언된 핸들러의 인스턴스를 만들 수 없습니다. - method: %s", method.getName());
    }
}
