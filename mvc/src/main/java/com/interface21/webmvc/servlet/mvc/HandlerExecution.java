package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;

import java.lang.reflect.Method;
import java.util.Arrays;

public class HandlerExecution {

    private final Object declaredObject;
    private final Method method;

    public HandlerExecution(Object declaredObject, Method method) {
        if (!ModelAndView.class.isAssignableFrom(method.getReturnType())
                || !Arrays.equals(method.getParameterTypes(),
                new Class<?>[]{HttpServletRequest.class, HttpServletResponse.class})) {
            throw new IllegalArgumentException("Invalid @RequestMapping method: " + method
                    + ". Expected ModelAndView return type and (HttpServletRequest, HttpServletResponse) parameters");
        }
        this.declaredObject = declaredObject;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(declaredObject, request, response);
    }
}
