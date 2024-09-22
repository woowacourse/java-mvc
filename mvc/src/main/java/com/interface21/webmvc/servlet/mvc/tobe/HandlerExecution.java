package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;

public class HandlerExecution {

    private final Object instance;
    private final Method handlerMethod;

    public HandlerExecution(Object instance, Method handlerMethod) {
		this.instance = instance;
		this.handlerMethod = handlerMethod;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView)handlerMethod.invoke(instance, request, response);
    }
}
