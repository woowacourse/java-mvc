package com.interface21.webmvc.servlet.mvc.handleradaptor;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.handlermapping.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdaptor implements HandlerAdaptor {

    @Override
    public boolean support(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return ((HandlerExecution) handler).handle(request, response);
    }
}
