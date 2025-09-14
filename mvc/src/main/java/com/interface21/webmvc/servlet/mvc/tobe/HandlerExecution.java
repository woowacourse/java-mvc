package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

public class HandlerExecution {

    private final RequestMethod[] methods;
    private final Map<String,String> values;

    public HandlerExecution(RequestMethod[] methods) {
        this.methods = methods;
        values = new HashMap<>();
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        JspView jspView = new JspView(request.getRequestURI());

        ModelAndView modelAndView = new ModelAndView(jspView);

        String id = (String) request.getAttribute("id");

        modelAndView.addObject("id",id);
        return modelAndView;
    }
}
