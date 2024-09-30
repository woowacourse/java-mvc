package com.interface21.webmvc.servlet.mvc.handler.controller;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.handler.HandlerAdapter;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        String viewName = ((Controller) handler).execute(req, res);

        return new ModelAndView(new JspView(viewName));
    }
}
