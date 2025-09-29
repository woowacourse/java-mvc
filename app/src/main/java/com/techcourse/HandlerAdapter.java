package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerAdapter {

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        if (handler instanceof Controller) {
            String viewName = ((Controller)handler).execute(request, response);
            return new ModelAndView(new JspView(viewName));
        } else if (handler instanceof HandlerExecution) {
            return ((HandlerExecution)handler).handle(request, response);
        } else {
            throw new IllegalArgumentException("No handler found for request");
        }
    }
}
