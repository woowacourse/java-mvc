package com.interface21.webmvc.servlet.handler.adapter;


import java.util.ArrayList;
import java.util.List;

// handlerAdapter 목록 관리
public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry(final List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public static HandlerAdapterRegistry empty() {
        return new HandlerAdapterRegistry(new ArrayList<>());
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object object) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(object))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No adapter found for handling this request."));
    }
}

