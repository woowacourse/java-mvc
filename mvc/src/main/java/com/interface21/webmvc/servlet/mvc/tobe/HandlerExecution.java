package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerExecution {

    private static final Logger log = LoggerFactory.getLogger(HandlerExecution.class);

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final ModelAndView modelAndView = new ModelAndView(new JspView(request.getRequestURI() + ".jsp"));
        modelAndView.addObject("id", request.getAttribute("id"));

        return modelAndView;
    }
}
