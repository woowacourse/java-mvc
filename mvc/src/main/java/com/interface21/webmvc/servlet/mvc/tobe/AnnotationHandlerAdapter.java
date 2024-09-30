package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.ModelAndView;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    public boolean canHandle(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    public ModelAndView handle(final Object handler, final HttpServletRequest req, final HttpServletResponse res)
            throws Exception {
        if (canHandle(handler)) {
            return ((HandlerExecution) handler).handle(req, res);
        }
        throw new IllegalArgumentException("실행할 수 없는 타입의 handler 입니다.");
    }
}
