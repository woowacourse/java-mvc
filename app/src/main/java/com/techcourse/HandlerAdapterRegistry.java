package com.techcourse;

import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry(List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No suitable handler adapter found for " + handler.getClass()));
    }

    @Override
    public String toString() {
        return "HandlerAdapterRegistry{" +
                "handlerAdapters=" + handlerAdapters +
                '}';
    }
}
