package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ManualHandlerExecution implements HandlerExecution {

    private final Controller controller;

    public ManualHandlerExecution(final Controller controller) {
        this.controller = controller;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final String viewName = controller.execute(request, response);
        return new ModelAndView(new JspView(viewName));
    }
}
