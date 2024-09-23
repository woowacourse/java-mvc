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
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HandlerExecution handlerExecution = castHandler(handler);
        return handlerExecution.handle(request, response);
    }
}
