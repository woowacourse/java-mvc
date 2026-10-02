package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class HandlerExecution {

    private static final Class<?>[] PARAMETER_TYPES = {HttpServletRequest.class, HttpServletResponse.class};

    private final Object controller;
    private final Method method;

    public HandlerExecution(final Object controller, final Method method) {
        validate(method);
        this.controller = controller;
        this.method = method;
    }

    private void validate(final Method method) {
        if (!Modifier.isPublic(method.getModifiers())) {
            throw new IllegalStateException("핸들러 메서드는 public이어야 합니다: " + method);
        }
        if (!Arrays.equals(method.getParameterTypes(), PARAMETER_TYPES)) {
            throw new IllegalStateException("핸들러 메서드의 파라미터는 (HttpServletRequest, HttpServletResponse)여야 합니다: " + method);
        }
        if (!ModelAndView.class.isAssignableFrom(method.getReturnType())) {
            throw new IllegalStateException("핸들러 메서드는 ModelAndView를 반환해야 합니다: " + method);
        }
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        try {
            return (ModelAndView) method.invoke(controller, request, response);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof Exception cause) {
                throw cause;
            }
            if (e.getCause() instanceof Error cause) {
                throw cause;
            }
            throw e;
        }
    }
}
