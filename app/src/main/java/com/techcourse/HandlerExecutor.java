package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Set;

public class HandlerExecutor {

    private static final String NOT_FOUND_VIEW = "404.jsp";

    private static final Set<HandlerAdapter> handlerAdapters = Set.of(
            new ControllerAdapter(),
            new HandlerExecutionAdapter()
    );

    public String execute(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.supports(handler)) {
                return handlerAdapter.adapt(request, response, handler);
            }
        }
        return NOT_FOUND_VIEW;
    }
}
