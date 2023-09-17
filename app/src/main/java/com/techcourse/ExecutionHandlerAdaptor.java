package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ExecutionHandlerAdaptor implements HandlerAdaptor {

    @Override
    public boolean isSame(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public String execute(final Object handler, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final HandlerExecution handlerExecution = (HandlerExecution) handler;
        final ModelAndView modelAndView = handlerExecution.handle(request, response);

        final View view = modelAndView.getView();

        if (view instanceof JspView) {
            final JspView jspView = (JspView) view;
            return jspView.getViewName();
        }

        return null;
    }
}
