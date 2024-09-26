package com.interface21.webmvc.servlet.mvc.tobe.handler.mapping;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerMappings {

    private static final Logger log = LoggerFactory.getLogger(HandlerMappings.class);

    private final List<HandlerMapping> values;

    public HandlerMappings(List<HandlerMapping> values) {
        this.values = values;
    }

    public static HandlerMappings createFromBasePackages(Object... basePackage) {
        return of(basePackage, List.of());
    }

    public static HandlerMappings of(Object[] basePackage, List<HandlerMapping> additionalMappings) {
        List<HandlerMapping> handlerMappings = new ArrayList<>(additionalMappings);
        handlerMappings.add(new AnnotationHandlerMapping(basePackage));

        return new HandlerMappings(handlerMappings);
    }

    public Object getHandler(HttpServletRequest request) throws ServletException {
        return values.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> {
                    log.error("No handler found for request: {}", request);
                    return new ServletException("No handler found for request");
                });
    }

    public void initialize() {
        values.forEach(HandlerMapping::initialize);
    }
}
