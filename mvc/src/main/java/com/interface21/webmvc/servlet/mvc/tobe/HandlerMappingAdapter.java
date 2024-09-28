package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerMappingAdapter implements HandlerMapping {

    private final AnnotationHandlerMapping annotationHandlerMapping;

    public HandlerMappingAdapter(AnnotationHandlerMapping annotationHandlerMapping) {
        this.annotationHandlerMapping = annotationHandlerMapping;
    }

    @Override
    public void initialize() {
        annotationHandlerMapping.initialize();
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            HandlerExecution handlerExecution = (HandlerExecution) annotationHandlerMapping.getHandler(request);
            return handlerExecution.handle(request, response);
        } catch (Exception e) {
            throw new IllegalArgumentException("컨트롤러 동작 수행 중 문제가 발생했습니다.", e);
        }
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return annotationHandlerMapping.getHandler(request);
    }

    @Override
    public boolean containsRequest(HttpServletRequest request) {
        try {
            annotationHandlerMapping.getHandler(request);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
