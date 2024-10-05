package com.interface21.webmvc.servlet.mvc.mapping.impl;

import java.lang.reflect.Method;

public class HandlerExecutionFailedException extends RuntimeException {

    public HandlerExecutionFailedException(Object handler, Method method, Throwable ex) {
        super(getMessage(handler, method), ex);
    }

    private static String getMessage(Object handler, Method method) {
        return String.format("핸들러 실행에 실패했습니다. - handler: %s, method: %s", handler.getClass().getName(),
                method.getName());
    }
}
