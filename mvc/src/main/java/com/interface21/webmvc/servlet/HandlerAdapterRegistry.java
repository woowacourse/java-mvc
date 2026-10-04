package com.interface21.webmvc.servlet;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        if (handlerAdapters.stream()
                .anyMatch(registered -> registered.getClass() == handlerAdapter.getClass())) {
            throw new IllegalArgumentException("이미 등록된 핸들러 어댑터입니다: " + handlerAdapter.getClass().getSimpleName());
        }

        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "사용할 수 있는 핸들러 어댑터가 없습니다: " + handler.getClass().getName()));
    }
}
