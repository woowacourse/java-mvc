package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return ((HandlerExecution) handler).handle(request, response);
    }

    @Override
    public boolean canHandle(Object handler) {
        return handler instanceof HandlerExecution;
    }
}
