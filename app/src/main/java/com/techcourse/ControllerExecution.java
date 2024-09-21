package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.Execution;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerExecution implements Execution {

    private final Controller controller;

    public ControllerExecution(Controller controller) {
        this.controller = controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String executed = controller.execute(request, response);
        JspView view = new JspView(executed);
        return new ModelAndView(view);
    }
}
