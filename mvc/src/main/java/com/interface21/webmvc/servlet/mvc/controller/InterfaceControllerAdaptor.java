package com.interface21.webmvc.servlet.mvc.controller;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdaptor;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class InterfaceControllerAdaptor implements HandlerAdaptor {

    @Override
    public boolean canExecute(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView execute(final Object handler, final HttpServletRequest request,
                                final HttpServletResponse response) throws Exception {
        String viewName = ((Controller) handler).execute(request, response);
        JspView view = new JspView(viewName);
        return new ModelAndView(view);
    }
}
