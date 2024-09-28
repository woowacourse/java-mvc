package com.interface21.webmvc.servlet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public HandlerAdapters(HandlerAdapter... handlerAdapters) {
        this.handlerAdapters.addAll(Arrays.asList(handlerAdapters));
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("핸들러 어댑터가 존재하지 않습니다. " + handler.toString()));
    }
}
