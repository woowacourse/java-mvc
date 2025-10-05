package com.interface21.webmvc.servlet.handler;

import com.interface21.webmvc.servlet.handler.annotationbase.container.AnnotationBaseHandlerContainer;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerContainerFacade {

    private final List<HandlerContainer> handlerContainers = new ArrayList<>();

    public HandlerContainerFacade(Object... basePackages) {
        AnnotationBaseHandlerContainer annotationBaseHandlerContainer = new AnnotationBaseHandlerContainer();
        annotationBaseHandlerContainer.initialize(basePackages);
        handlerContainers.add(annotationBaseHandlerContainer);
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerContainers.stream()
                .map(container -> container.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("핸들러를 찾을 수 없습니다: " + request.getRequestURI()));
    }
}
