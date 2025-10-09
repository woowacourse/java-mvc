package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class MethodHandler {

    private final Object controller;
    private final Method method;

    public MethodHandler(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public Object handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Object[] arguments = resolveArguments(request, response);
        return method.invoke(controller, arguments);
    }

    public Method getMethod() {
        return method;
    }

    private Object[] resolveArguments(final HttpServletRequest request, final HttpServletResponse response) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final Object[] args = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];

            if (HttpServletRequest.class.isAssignableFrom(parameterType)) {
                args[i] = request;
                continue;
            }

            if (HttpServletResponse.class.isAssignableFrom(parameterType)) {
                args[i] = response;
                continue;
            }

            throw new IllegalArgumentException("지원하지 않는 파라미터 타입입니다: " + parameterType);
        }

        return args;
    }
}
