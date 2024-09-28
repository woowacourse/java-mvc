package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object declaredObject;
    private final Method method;

    public HandlerExecution(Object declaredObject, Method method) {
        this.declaredObject = declaredObject;
        this.method = method;
    }

    ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            return (ModelAndView) method.invoke(declaredObject, request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
