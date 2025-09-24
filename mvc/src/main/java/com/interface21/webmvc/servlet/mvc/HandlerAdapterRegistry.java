package com.interface21.webmvc.servlet.mvc;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void addHandlerAdapter(final HandlerAdapter adapter) {
        handlerAdapters.add(adapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        for (HandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new IllegalStateException("No adapter found for handler: " + handler);
    }
}
