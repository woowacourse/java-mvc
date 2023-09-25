package com.techcourse;

import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> adapters;

    public HandlerAdapters() {
        this.adapters = new ArrayList<>();
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        adapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return adapters.stream()
                .filter(it -> it.supports(handler))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
