package com.interface21.webmvc.servlet.mvc.tobe.handleradapter;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void register(final HandlerAdapter handlerAdapter) {
        this.handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.isAdaptable(handler)) {
                return handlerAdapter;
            }
        }
        throw new IllegalStateException(handler.getClass().getSimpleName() + "를 실행할 수 있는 어댑터가 등록되어 있지 않습니다: ");
    }
}
