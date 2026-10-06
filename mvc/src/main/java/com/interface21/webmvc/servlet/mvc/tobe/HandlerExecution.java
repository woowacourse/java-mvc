package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class HandlerExecution {

    private final Object controller;
    private final Method method;

    public HandlerExecution(Object controller, Method method) {
        Class<?>[] expectedParameters = {
                HttpServletRequest.class,
                HttpServletResponse.class
        };
        if (!Arrays.equals(method.getParameterTypes(), expectedParameters)
                || method.getReturnType() != ModelAndView.class) {
            throw new IllegalStateException("잘못된 핸들러 메서드 시그니처: " + method.toGenericString());
        }

        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        Object result;
        try {
            result = method.invoke(controller, request, response);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof Exception exception) {
                throw exception;
            }
            if (cause instanceof Error error) {
                throw error;
            }
            throw e;
        }
        if (result instanceof ModelAndView modelAndView) {
            return modelAndView;
        }

        throw new IllegalStateException("핸들러 메서드가 ModelAndView를 반환하지 않았습니다: " + method.toGenericString());
    }
}
