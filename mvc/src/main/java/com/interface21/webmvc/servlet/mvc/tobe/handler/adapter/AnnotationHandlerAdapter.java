package com.interface21.webmvc.servlet.mvc.tobe.handler.adapter;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.handler.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdapter extends AbstractHandlerAdapter<HandlerExecution> {

    public AnnotationHandlerAdapter() {
        super(HandlerExecution.class);
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, HandlerExecution handler)
            throws Exception {
        return handler.handle(request, response);
    }
}
