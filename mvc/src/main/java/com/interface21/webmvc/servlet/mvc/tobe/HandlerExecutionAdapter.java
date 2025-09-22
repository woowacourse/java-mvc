package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutionAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        final var result = ((HandlerExecution) handler).handle(request, response);

        if (result instanceof ModelAndView) {
            return (ModelAndView) result;
        }
        if (result instanceof String) {
            return new ModelAndView(new JspView((String) result));
        }
        throw new IllegalStateException("Unsupported return type: " + result.getClass().getName());
    }
}
