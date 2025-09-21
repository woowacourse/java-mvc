package com.interface21.webmvc.servlet.mvc.tobe.handleradapter;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    public ModelAndView execute(
            final Object handler,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {

        final var handlerExecution = (HandlerExecution) handler;
        return handlerExecution.handle(request, response);
    }
}
