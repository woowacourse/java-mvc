package com.interface21.webmvc.servlet.handler;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.handler.annotationbase.processor.AnnotationBaseHandlerProcessor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

public class HandlerProcessorFacade {

    private final List<HandlerProcessor> handlerProcessor = new ArrayList<>();

    public HandlerProcessorFacade() {
        handlerProcessor.add(new AnnotationBaseHandlerProcessor());
    }

    public ModelAndView process(
            final Object handler,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        return handlerProcessor.stream()
                .filter(processor -> processor.supports(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원되지 않는 핸들러입니다: " + handler.getClass()))
                .process(handler, request, response);
    }
}
