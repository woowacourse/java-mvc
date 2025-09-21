package com.interface21.webmvc.servlet.mvc;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry(final List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public static HandlerAdapterRegistry createEmpty() {
        return new HandlerAdapterRegistry(new ArrayList<>());
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object object) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.canHandle(object))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("요청을 처리할 수 없습니다."));
    }
}
