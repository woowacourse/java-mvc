package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
아래 흐름 담당
HandlerExecution
     ↓
handle()
     ↓
ModelAndView
 */
public class HandlerExecutionAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        // Annotation MVC의 HandlerExecution만 처리한다.
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(
            final Object handler,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {

        final HandlerExecution handlerExecution =
                (HandlerExecution) handler;

        // @MVC는 이미 ModelAndView를 반환하므로 그대로 전달한다.
        return handlerExecution.handle(
                request,
                response
        );
    }
}
