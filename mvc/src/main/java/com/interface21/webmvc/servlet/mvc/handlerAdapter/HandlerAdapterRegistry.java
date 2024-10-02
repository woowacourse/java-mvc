package com.interface21.webmvc.servlet.mvc.handlerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.interface21.webmvc.servlet.HandlerAdapter;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        handlerAdapters = new ArrayList<>();
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.support(handler))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No handlerAdapter found"));
    }
}
