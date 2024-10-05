package com.interface21.webmvc.servlet.mvc.handler.annotation;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object instance;
    private final Method handlerMethod;

    public HandlerExecution(Object instance, Method handlerMethod) {
        this.instance = instance;
        this.handlerMethod = handlerMethod;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) handlerMethod.invoke(instance, request, response);
    }
}
