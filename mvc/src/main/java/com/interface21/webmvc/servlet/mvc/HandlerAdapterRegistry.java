package com.interface21.webmvc.servlet.mvc;

import java.util.ArrayList;
import java.util.List;

public record HandlerAdapterRegistry(List<HandlerAdapter> handlerAdapters) {

    public HandlerAdapterRegistry() {
        this(new ArrayList<>());
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        this.handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No adapter for handler " + handler));
    }
}
