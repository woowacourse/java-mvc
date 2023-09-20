package com.techcourse.support.handler;

import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
            .filter(adapter -> adapter.supports(handler))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("Handler adapter Not Found: " + handler));
    }

}
