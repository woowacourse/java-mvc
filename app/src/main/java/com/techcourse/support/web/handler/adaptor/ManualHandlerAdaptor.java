package com.techcourse.support.web.handler.adaptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdaptor;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ManualHandlerAdaptor implements HandlerAdaptor {
    
    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView execute(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        final var controller = (Controller) handler;
        final var viewName = controller.execute(request, response);
        final var view = new JspView(viewName);
        return new ModelAndView(view);
    }
}
