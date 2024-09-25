package com.interface21.webmvc.servlet.mvc.tobe.handler.adapter;

import jakarta.servlet.ServletException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerAdapters {

    private static final Logger log = LoggerFactory.getLogger(HandlerAdapters.class);

    private final Set<HandlerAdapter> values;

    public HandlerAdapters(HandlerAdapter... additionalMappings) {
        List<HandlerAdapter> handlerAdapters = new ArrayList<>(Arrays.asList(additionalMappings));
        handlerAdapters.add(new AnnotationHandlerAdapter());
        handlerAdapters.sort(this::compareHandlerAdapters);

        this.values = new LinkedHashSet<>(handlerAdapters);
    }

    private int compareHandlerAdapters(HandlerAdapter first, HandlerAdapter second) {
        Class<?> firstHandlerType = first.getHandlerType();
        Class<?> secondHandlerType = second.getHandlerType();

        boolean firstSupportsSecond = firstHandlerType.isAssignableFrom(secondHandlerType);
        boolean secondSupportsFirst = secondHandlerType.isAssignableFrom(firstHandlerType);

        return Boolean.compare(firstSupportsSecond, secondSupportsFirst);
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
