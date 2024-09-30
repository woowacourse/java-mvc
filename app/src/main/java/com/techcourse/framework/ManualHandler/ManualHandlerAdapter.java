package com.techcourse.framework.ManualHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;

public class ManualHandlerAdapter implements HandlerAdapter {

    private final Controller controller;

    public ManualHandlerAdapter(Controller controller) {
        this.controller = controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String execute = controller.execute(request, response);
        return new ModelAndView(new JspView(execute));
    }
}
