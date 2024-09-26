package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerHandlerAdapter implements HandlerExecution {

    private final Controller controller;

    public ControllerHandlerAdapter(Controller controller) {
        this.controller = controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
        try {
            String viewName = controller.execute(request, response);
            return new ModelAndView(new JspView(viewName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
