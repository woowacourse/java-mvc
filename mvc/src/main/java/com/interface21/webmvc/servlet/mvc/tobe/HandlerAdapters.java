package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.HashSet;
import java.util.List;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;

public class HandlerAdapters {

    private final HashSet<HandlerAdapter> adapters;

    public HandlerAdapters(HandlerAdapter... handlerAdapters) {
        this.adapters = new HashSet<>(List.of(handlerAdapters));
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter adapter : adapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }

        throw new IllegalStateException("Unsupported handler type: " + handler);
    }
}
