package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdaptor implements HandlerAdaptor {

    private final HandlerExecution handlerExecution;

    public AnnotationHandlerAdaptor(Object handler) {
        if (!isSupport(handler)) {
            throw new RuntimeException("지원하지 않는 핸들러 입니다.");
        }
        this.handlerExecution = (HandlerExecution) handler;
    }

    @Override
    public boolean isSupport(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return handlerExecution.handle(request, response);
    }
}
