package com.techcourse.handlermapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.view.JspView;

public class HandlerExecutionAdapter implements HandlerExecution {

    private final Controller controller;

    public HandlerExecutionAdapter(Controller controller) {
        this.controller = controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String result = controller.execute(request, response);
        return new ModelAndView(new JspView(result));
    }
}
