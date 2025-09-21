package com.techcourse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandlerAdaptorRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdaptorRegistry() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public Optional<HandlerAdapter> getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst();
    }
}
