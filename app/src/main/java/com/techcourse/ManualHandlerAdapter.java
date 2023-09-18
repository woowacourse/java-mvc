package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ManualHandlerAdapter implements HandlerAdapter {

    private final Object handler;

    public ManualHandlerAdapter(Object handler) {
        this.handler = handler;
    }

    @Override
    public boolean supports(Object handler) {
        return handler instanceof ForwardController;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView(new JspView(((ForwardController) handler).execute(request, response)));
    }
}
