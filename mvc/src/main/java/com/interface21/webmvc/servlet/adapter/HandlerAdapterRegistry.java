package com.interface21.webmvc.servlet.adapter;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {
    private List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void addHandlerAdapter(HandlerAdapter handlerAdapter){
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
