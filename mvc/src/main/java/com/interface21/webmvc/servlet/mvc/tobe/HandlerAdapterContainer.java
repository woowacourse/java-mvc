package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandlerAdapterContainer {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterContainer() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void initialize() {
        addHandlerAdapter(new ExecutionHandlerAdapter());
        addHandlerAdapter(new ControllerHandlerAdapter());
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public Optional<HandlerAdapter> getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findAny();
    }
}
