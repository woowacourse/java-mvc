package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object controller;
    private final Method handler;

    public HandlerExecution(final Object controller, final Method handler) {
        this.controller = controller;
        this.handler = handler;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object ret = handler.invoke(controller, request, response);

        if (ret instanceof ModelAndView modelAndView) {
            return modelAndView;
        }

        if (ret instanceof String viewName) {
            return new ModelAndView(new JspView(viewName));
        }

        throw new UnsupportedOperationException();
    }
}
