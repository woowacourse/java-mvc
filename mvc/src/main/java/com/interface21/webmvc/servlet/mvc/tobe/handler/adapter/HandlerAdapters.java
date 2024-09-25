package com.interface21.webmvc.servlet.mvc.tobe.handler.adapter;

import jakarta.servlet.ServletException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerAdapters {

    private static final Logger log = LoggerFactory.getLogger(HandlerAdapters.class);

    private final Set<HandlerAdapter> values;

    public HandlerAdapters(HandlerAdapter... handlerAdapters) {
        this.values = new LinkedHashSet<>(Arrays.asList(handlerAdapters));
        values.add(new AnnotationHandlerAdapter());
    }

    public HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
        return values.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("No suitable adapter found for handler: {}", handler);
                    return new ServletException("No suitable adapter found for handler");
                });
    }
}
