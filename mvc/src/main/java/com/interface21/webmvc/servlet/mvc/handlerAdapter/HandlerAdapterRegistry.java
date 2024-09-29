package com.interface21.webmvc.servlet.mvc.handlerAdapter;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object object) {
        return handlerAdapters.stream()
                .filter(handlerAdapters -> handlerAdapters.isSupported(object))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Handler에 맞는 HandlerAdapter를 찾을 수 없습니다."));
    }
}
