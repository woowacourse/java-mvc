package com.interface21.webmvc.servlet.mvc.tobe.handlerAdapter;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.canHandle(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("no handler adapter found."));
    }
}
