package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.handleradapter.HandlerAdapter;
import java.util.ArrayList;
import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter findHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(a -> a.supports(handler))
                .findAny()
                .orElseThrow(() -> new UnsupportedOperationException("처리할 수 없는 요청입니다."));
    }
}
