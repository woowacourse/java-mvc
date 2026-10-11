package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutionAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(
            final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        if (!(handler instanceof HandlerExecution execution)) {
            throw new IllegalArgumentException("HandlerExecution 타입의 실행 대상만 처리할 수 있습니다.");
        }
        return execution.handle(request, response);
    }
}
