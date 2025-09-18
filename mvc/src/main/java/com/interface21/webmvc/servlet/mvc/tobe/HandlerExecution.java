package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import java.util.Enumeration;

public class HandlerExecution {

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        String requestURI = request.getRequestURI();
        ModelAndView modelAndView = new ModelAndView(new JspView(requestURI + ".jsp"));
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }
}
