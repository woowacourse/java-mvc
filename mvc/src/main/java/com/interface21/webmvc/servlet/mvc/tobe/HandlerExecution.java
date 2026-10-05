package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class HandlerExecution {

    private final Object controller;
    private final Method method;

    public HandlerExecution(final Object controller, final Method method) {
        validatePublic(method);
        validateParameters(method);
        validateReturnType(method);
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(controller, request, response);
    }

    private void validatePublic(final Method method) {
        int modifiers = method.getModifiers();
        if (!Modifier.isPublic(modifiers)) {
            throw new IllegalStateException("invalidAccess: 핸들러 메서드는 public 메서드여야 합니다: " + method);
        }
    }


    private void validateParameters(final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();

        if (parameterTypes.length != 2
                || parameterTypes[0] != HttpServletRequest.class
                || parameterTypes[1] != HttpServletResponse.class) {
            throw new IllegalStateException(
                    "invalidAccess: 핸들러 메서드의 인자는 (HttpServletRequest, HttpServletResponse)여야 합니다: " + method);
        }
    }

    private void validateReturnType(final Method method) {
        if (!ModelAndView.class.isAssignableFrom(method.getReturnType())) {
            throw new IllegalStateException("invalidAccess: 핸들러 메서드는 ModelAndView를 반환해야 합니다: " + method);
        }
    }
}
