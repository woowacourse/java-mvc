package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean support(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!support(handler)) {
            throw new IllegalArgumentException("지원하지 않는 핸들러입니다.");
        }
        return ((HandlerExecution) handler).handle(request, response);
    }
}
