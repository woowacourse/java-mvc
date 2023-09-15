package com.techcourse.support.mvc.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.adapter.HandlerAdapter;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ManualHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean isSupport(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                               final Object handler) throws Exception {
        Controller controller = (Controller) handler;
        String viewName = controller.execute(request, response);
        return new ModelAndView(new JspView(viewName));
    }
}
