package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.ModelAndView;

public class HandlerExecution {
    private final Method method;
    private final Class<?> clazz;

    public HandlerExecution(Method method, Class<?> clazz) {
        this.method = method;
        this.clazz = clazz;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object controller = clazz.getDeclaredConstructor().newInstance();
        return (ModelAndView) method.invoke(controller, request, response);
    }
}
