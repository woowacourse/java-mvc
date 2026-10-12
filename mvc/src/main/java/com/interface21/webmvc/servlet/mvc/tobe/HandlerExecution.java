package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class HandlerExecution {

    private final Object controller;
    private final Method method;

    public HandlerExecution(final Object controller, final Method method) {
        validate(method);
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(controller, request, response);
    }

    private void validate(final Method method) {
        final boolean validParameters = Arrays.equals(method.getParameterTypes(),
                new Class<?>[]{HttpServletRequest.class, HttpServletResponse.class});
        if (!Modifier.isPublic(method.getModifiers())
                || !validParameters
                || !ModelAndView.class.isAssignableFrom(method.getReturnType())) {
            throw new IllegalStateException("Invalid handler method: "
                    + method.getDeclaringClass().getSimpleName() + "." + method.getName());
        }
    }
}
