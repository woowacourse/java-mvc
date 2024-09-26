package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private static final HandlerAdapterRegistry INSTANCE = new HandlerAdapterRegistry();

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    private HandlerAdapterRegistry() {
    }

    public static HandlerAdapterRegistry getInstance() {
        return INSTANCE;
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElse(null);
    }
}
