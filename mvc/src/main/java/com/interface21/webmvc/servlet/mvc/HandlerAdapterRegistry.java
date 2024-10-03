package com.interface21.webmvc.servlet.mvc;

import java.util.ArrayList;
import java.util.List;

import com.interface21.webmvc.servlet.HandlerAdapter;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new ArrayList<>();
        initialize();
    }

    private void initialize() {
        handlerAdapters.add(new HandlerExecutionHandlerAdapter());
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 핸들러를 처리할 수 있는 어댑터가 없습니다."));
    }
}
