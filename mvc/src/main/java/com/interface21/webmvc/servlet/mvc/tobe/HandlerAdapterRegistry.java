package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.List;

public class HandlerAdapterRegistry {

    private List<HandlerAdapter> registry;

    public HandlerAdapterRegistry(final List<HandlerAdapter> registry) {
        this.registry = registry;
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        registry.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return registry.stream()
                .filter(handlerAdapter -> handlerAdapter.canHandle(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("실행할 수 없는 타입의 handler 입니다."));
    }
}
