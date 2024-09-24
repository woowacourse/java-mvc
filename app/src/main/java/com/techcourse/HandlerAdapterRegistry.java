package com.techcourse;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
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
