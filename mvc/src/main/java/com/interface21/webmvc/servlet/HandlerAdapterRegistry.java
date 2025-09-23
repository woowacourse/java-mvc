package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.handler.adapter.HandlerAdapter;
import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream().filter(handlerAdapter -> handlerAdapter.isHandlingPossible(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("핸들러에 맞는 핸들러 어뎁터가 존재하지 않습니다."));
    }
}
