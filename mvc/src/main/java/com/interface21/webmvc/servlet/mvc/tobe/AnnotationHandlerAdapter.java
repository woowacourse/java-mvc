package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public ModelAndView handle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) throws Exception {
        if (!(handler instanceof HandlerExecution)) {
            throw new UnsupportedOperationException("지원하지 않는 핸들러 타입입니다. : " + handler);
        }

        return ((HandlerExecution) handler).handle(request, response);
    }

    @Override
    public boolean canHandle(final Object handler) {
        return handler instanceof HandlerExecution;
    }
}
