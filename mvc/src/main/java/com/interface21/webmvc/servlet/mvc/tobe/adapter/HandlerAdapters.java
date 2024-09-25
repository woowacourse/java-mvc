package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters() {
        this.handlerAdapters = new ArrayList<>();
        initHandlerAdapters();
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new ControllerHandlerAdapter());
        handlerAdapters.add(new HandlerExecutionHandlerAdapter());
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("핸들러를 처리할 수 있는 Adapter가 존재하지 않습니다."));
    }
}
