package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.HandlerAdaptor;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdaptor implements HandlerAdaptor {

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof HandlerExecution);
    }

    @Override
    public ModelAndView handle(HttpServletRequest req, HttpServletResponse resp, Object handler) {
        if (supports(handler)) {
            return ((HandlerExecution) handler).handle(req, resp);
        }
        throw new IllegalArgumentException("Fail to Handler Adapting");
    }
}
