package com.interface21.webmvc.servlet.mvc.tobe.execution;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object controller;
    private final Method method;

    public HandlerExecution(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return (ModelAndView) method.invoke(controller, req, resp);
    }
}
