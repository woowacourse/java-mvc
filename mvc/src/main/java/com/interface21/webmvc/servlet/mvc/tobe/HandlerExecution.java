package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private Object controller;
    private Method requestMethod;

    public HandlerExecution(Object controller, Method requestMethod) {
        this.controller = controller;
        this.requestMethod = requestMethod;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object invoked = requestMethod.invoke(controller, request, response);
        return (ModelAndView) invoked;
    }
}
