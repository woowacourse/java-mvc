package com.interface21.webmvc.servlet.mvc.handler;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdaptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationControllerAdaptor implements HandlerAdaptor {

    @Override
    public boolean canExecute(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView execute(final Object handler, final HttpServletRequest request,
                                final HttpServletResponse response) throws Exception {
        return ((HandlerExecution) handler).handle(request, response);
    }
}
