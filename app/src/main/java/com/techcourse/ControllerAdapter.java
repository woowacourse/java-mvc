package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.handlermapping.HandlerAdapter;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ControllerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(
        Object handler,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        String viewName = ((Controller) handler).execute(request, response);
        JspView jspView = new JspView(viewName);
        return new ModelAndView(jspView);
    }
}
