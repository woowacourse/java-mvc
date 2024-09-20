package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerExecution {

    private static final Logger log = LoggerFactory.getLogger(HandlerExecution.class);

    private final String viewName;

    public HandlerExecution(String viewName) {
        this.viewName = viewName;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        log.info("handler execution handle");
        ModelAndView modelAndView = new ModelAndView(new JspView(viewName));
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }
}
