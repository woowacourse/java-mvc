package com.interface21.webmvc.servlet.mvc.controller;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdaptor;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerAdaptor implements HandlerAdaptor {

    @Override
    public boolean canExecute(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView execute(Object handler, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String viewName = ((Controller) handler).execute(request, response);
        JspView view = new JspView(viewName);
        return new ModelAndView(view);
    }
}
