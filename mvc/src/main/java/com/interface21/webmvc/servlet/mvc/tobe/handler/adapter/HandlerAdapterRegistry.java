package com.interface21.webmvc.servlet.mvc.tobe.handler.adapter;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import jakarta.servlet.ServletException;
import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new ArrayList<>();

        addHandlerAdapter(new ManualHandlerAdapter());
        addHandlerAdapter(new AnnotationHandlerAdapter());
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) throws ServletException {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElseThrow(
                        () -> new ServletException("No handler adapter")
                );
    }

    private void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }
}
