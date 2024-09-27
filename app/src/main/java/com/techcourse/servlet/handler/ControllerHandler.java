package com.techcourse.servlet.handler;

import com.interface21.webmvc.servlet.Handler;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerHandler implements Handler {

    private final Controller controller;

    public ControllerHandler(Controller controller) {
        this.controller = controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String viewName = controller.execute(req, res);
        JspView view = new JspView(viewName);
        return new ModelAndView(view);
    }
}
