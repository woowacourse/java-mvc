package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.List;

import com.interface21.context.container.Container;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        Container container = Container.getInstance();
        handlerAdapters = container.getInstancesOf(HandlerAdapter.class);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No handler adapter found for " + handler));
    }
}
