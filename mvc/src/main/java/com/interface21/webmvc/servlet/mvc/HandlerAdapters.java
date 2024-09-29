package com.interface21.webmvc.servlet.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters() {
        handlerAdapters = new ArrayList<>();
    }

    public void initialize() {
        handlerAdapters.add(new AnnotationHandlerAdapter());
    }

    public Optional<HandlerAdapter> findHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findAny();
    }
}
