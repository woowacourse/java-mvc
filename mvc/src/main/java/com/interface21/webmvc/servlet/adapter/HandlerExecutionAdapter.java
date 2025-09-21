package com.interface21.webmvc.servlet.adapter;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutionAdapter implements HandlerAdapter {
    @Override
    public ModelAndView handle(
            final Object handler,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        if (!(handler instanceof HandlerExecution)) {
            throw new IllegalStateException("AnnotationHandlerAdapter HandlerExecution 클래스만을 실행할 수 있습니다");
        }
        return ((HandlerExecution) handler).handle(request, response);
    }
}
