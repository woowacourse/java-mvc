package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object handlerInstance;
    private final Method method;

    public HandlerExecution(Method method) throws Exception {
        Class<?> clazz = method.getDeclaringClass();
        this.handlerInstance = clazz.getConstructor().newInstance();
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(handlerInstance, request, response);
    }
}
