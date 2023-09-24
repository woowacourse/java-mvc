package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ManualHandlerExecution implements HandlerExecution {

    private final Object controller;
    private final Method method;

    public ManualHandlerExecution(final Object controller, final Method method) {
        this.controller = controller;
        this.method = method;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final String viewName = (String) method.invoke(controller, request, response);
        return new ModelAndView(new JspView(viewName));
    }
}
