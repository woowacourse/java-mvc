package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry(final List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.support(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No adapter found for handler: " + handler.getClass().getSimpleName()));
    }
}
