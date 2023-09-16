package com.techcourse.servlet.adaptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ManualHandlerAdaptor implements HandlerAdaptor {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(final Object controller, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Controller interfaceController = (Controller) controller;
        final String url = interfaceController.execute(request, response);
        return new ModelAndView(new JspView(url));
    }
}
