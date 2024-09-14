package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecution {

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        String id = (String) request.getAttribute("id");
        ModelAndView modelAndView = new ModelAndView(new JspView(request.getRequestURI() + ".jsp"));
        modelAndView.addObject("id", id);
        return modelAndView;
    }
}
