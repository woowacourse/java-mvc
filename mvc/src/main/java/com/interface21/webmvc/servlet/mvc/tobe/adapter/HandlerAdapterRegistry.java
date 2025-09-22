package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 핸들러가 없습니다"));
    }
}
