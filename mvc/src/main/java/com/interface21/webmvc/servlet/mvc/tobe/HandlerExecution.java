package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.ModelAndView;

public class HandlerExecution {

    private final Method handler;

    public HandlerExecution(final Method handler) {
        this.handler = handler;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Class<?> controllerClass = handler.getDeclaringClass();
        Constructor<?> constructor = controllerClass.getConstructor();
        Object controller = constructor.newInstance();
        return (ModelAndView) handler.invoke(controller, request, response);
    }
}
