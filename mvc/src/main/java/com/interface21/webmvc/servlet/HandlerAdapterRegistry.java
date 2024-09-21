package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.adapter.ControllerInterfaceHandlerAdapter;
import com.interface21.webmvc.servlet.adapter.HandlerExecutionHandlerAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void initialize() {
        handlerAdapters.add(new HandlerExecutionHandlerAdapter());
        handlerAdapters.add(new ControllerInterfaceHandlerAdapter());
    }

    public void addHandlerAdapter(HandlerAdapter adapter) {
        handlerAdapters.add(adapter);
    }

    public Optional<HandlerAdapter> getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst();
    }
}
