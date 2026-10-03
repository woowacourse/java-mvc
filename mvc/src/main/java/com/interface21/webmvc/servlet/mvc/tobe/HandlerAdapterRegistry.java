package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry(final List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public static HandlerAdapterRegistry empty() {
        return new HandlerAdapterRegistry(new ArrayList<>());
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        if (handlerAdapter != null) {
            handlerAdapters.add(handlerAdapter);
        }
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
            .filter(handlerAdapter -> handlerAdapter.isSupported(handler))
            .findFirst()
            .orElseThrow(() -> new HandlerAdapterNotFoundException(
                "지원하는 핸들러 어댑터가 없습니다 (Handler: )" + handler.getClass()));
    }
}
