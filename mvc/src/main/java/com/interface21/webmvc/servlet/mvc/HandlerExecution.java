package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object controller;
    private final Method method;

    public HandlerExecution(final Object controller, final Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final Object[] arguments = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            final Class<?> parameterType = parameterTypes[i];
            if (parameterType == HttpServletRequest.class) {
                arguments[i] = request;
                continue;
            }
            if (parameterType == HttpServletResponse.class) {
                arguments[i] = response;
                continue;
            }
            throw new IllegalArgumentException(
                    "지원하지 않는 컨트롤러 파라미터 타입: " + parameterType.getTypeName()
                            + ", 메서드: " + method.toGenericString()
            );
        }

        final Object result = method.invoke(controller, arguments);

        if (result instanceof String viewName) {
            return new ModelAndView(new JspView(viewName));
        }
        if (result instanceof ModelAndView modelAndView) {
            return modelAndView;
        }
        throw new IllegalArgumentException(
                "지원하지 않는 컨트롤러 반환값 타입: " + (result == null ? "null" : result.getClass().getTypeName())
                        + ", 메서드: " + method.toGenericString()
        );
    }

    @Override
    public String toString() {
        return "HandlerExecution{method=" + method.toGenericString() + "}";
    }
}
