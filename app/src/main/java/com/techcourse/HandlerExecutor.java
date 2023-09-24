package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

import java.util.Set;

public class HandlerExecutor {

    private static final Set<HandlerAdapter> handlerAdapters = Set.of(
            new ControllerAdapter(),
            new HandlerExecutionAdapter()
    );

    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.supports(handler)) {
                return handlerAdapter.adapt(request, response, handler);
            }
        }
        throw new IllegalArgumentException("Not found handler adapter for handler : " + handler);
    }
}
