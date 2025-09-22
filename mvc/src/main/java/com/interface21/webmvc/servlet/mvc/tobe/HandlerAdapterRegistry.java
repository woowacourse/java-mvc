package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void add(HandlerAdapter handlerAdapter) {
        this.handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.canHandle(handler))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("존재하지 않는 핸들러입니다."));
    }
}
