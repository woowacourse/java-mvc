package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object view = ((HandlerExecution) handler).handle(request, response);
        if (view instanceof ModelAndView modelAndView) return modelAndView;
        else if (view instanceof String viewName) return new ModelAndView(viewName);
        throw new IllegalStateException("Handler Return Type Not Supported " + view.getClass().getSimpleName());
    }
}
