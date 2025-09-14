package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;

public class HandlerExecution {

    private final Object objectContainsHandler;
    private final Method handler;

    public HandlerExecution(Object objectContainsHandler, Method handler) {
        this.objectContainsHandler = objectContainsHandler;
        this.handler = handler;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) handler.invoke(objectContainsHandler, request, response);
    }
}
