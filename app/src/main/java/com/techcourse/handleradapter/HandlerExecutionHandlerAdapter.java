package com.techcourse.handleradapter;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.techcourse.HandlerAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutionHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        System.out.println("handler instanceof HandlerExecution = " + (handler instanceof HandlerExecution));
        return handler instanceof HandlerExecution;
    }

    @Override
    public Object handle(final HttpServletRequest request, final HttpServletResponse response,
                         final Object handler) throws Exception {
        return ((HandlerExecution) handler).handle(request, response);
    }
}
