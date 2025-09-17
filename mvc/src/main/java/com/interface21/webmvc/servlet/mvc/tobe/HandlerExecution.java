package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private Object controller;
    private Method method;

    public HandlerExecution(Object controller, Method method) {
        this.controller = controller;
        this.method = method;

        if (!this.method.canAccess(controller)) {
            this.method.setAccessible(true);
        }
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = (ModelAndView) method.invoke(controller, request, response);

        return modelAndView;
    }
}
