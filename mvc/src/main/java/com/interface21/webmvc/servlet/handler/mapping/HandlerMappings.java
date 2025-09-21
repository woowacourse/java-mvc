package com.interface21.webmvc.servlet.handler.mapping;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public class HandlerMappings {

    private static final String DEFAULT_PATH_OF_CONTROLLERS = "com.techcourse.controller";
    private final List<HandlerMapping> handlerMappings;

    private HandlerMappings(List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    public static HandlerMappings initialize() {
        AnnotationHandlerMapping annotationHandlerMapping = createAnnotationHandlerMappings();
        List<HandlerMapping> handlerMappings = List.of(
            annotationHandlerMapping
        );
        return new HandlerMappings(handlerMappings);
    }

    private static AnnotationHandlerMapping createAnnotationHandlerMappings() {
        AnnotationHandlerMapping mapping = new AnnotationHandlerMapping(DEFAULT_PATH_OF_CONTROLLERS);
        mapping.initialize();
        return mapping;
    }

    public Object getHandler(HttpServletRequest httpServletRequest) throws ServletException {
        for (HandlerMapping handlerMapping : this.handlerMappings) {
            Optional<Object> handler = handlerMapping.getHandler(httpServletRequest);
            if (handler.isPresent()) {
                return handler.get();
            }
        }
        throw new ServletException("제공하지 않는 URI입니다.");
    }
}
