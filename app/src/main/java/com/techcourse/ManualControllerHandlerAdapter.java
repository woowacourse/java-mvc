package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ManualControllerHandlerAdapter implements HandlerAdapter {

    private final Controller controller;

    public ManualControllerHandlerAdapter(final Object controller) {
        this.controller = (Controller) controller;
    }

    @Override
    public boolean isSupport(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final String path = controller.execute(request, response);
        return new ModelAndView(new JspView(path));
    }
}
