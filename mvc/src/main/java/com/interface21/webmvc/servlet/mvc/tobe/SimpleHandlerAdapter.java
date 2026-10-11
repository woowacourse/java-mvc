package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SimpleHandlerAdapter implements HandlerAdapter {

    @Override
    public ModelAndView handle(
            final Object handler,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        if (handler instanceof HandlerExecution handlerExecution) {
            return handlerExecution.handle(request, response);
        }

        throw new IllegalArgumentException("지원하지 않는 핸들러 타입입니다: " + handler);
    }
}