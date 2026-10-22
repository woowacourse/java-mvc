package com.interface21.webmvc.servlet;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        if (!handlerAdapters.contains(handlerAdapter)) {
            handlerAdapters.add(handlerAdapter);
        }
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.supports(handler)) {
                return handlerAdapter;
            }
        }
        throw new IllegalArgumentException("지원하는 HandlerAdapter가 없습니다.");
    }
}
