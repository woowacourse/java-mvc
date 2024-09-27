package com.interface21.webmvc.servlet.mvc.handler.adapter;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.canHandle(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("handler를 처리할 adapter가 존재하지 않습니다. handler = " + handler.getClass().getCanonicalName()));
    }
}
