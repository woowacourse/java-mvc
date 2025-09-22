package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import java.lang.reflect.Method;

public class HandlerExecution {

    Method method;
    Object handler;

    public HandlerExecution(final Method method, final Object handler) {
        this.method = method;
        this.handler = handler;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(handler, request, response);
    }
}
