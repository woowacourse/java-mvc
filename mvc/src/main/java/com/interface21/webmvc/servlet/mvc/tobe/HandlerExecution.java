package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;

public class HandlerExecution {

    public HandlerExecution() {
    }

    public ModelAndView handle(final HttpServletRequest request) {
        JspView jspView = new JspView(request.getRequestURI());

        ModelAndView modelAndView = new ModelAndView(jspView);

        String id = (String) request.getAttribute("id");
        modelAndView.addObject("id", id);

        return modelAndView;
    }
}
