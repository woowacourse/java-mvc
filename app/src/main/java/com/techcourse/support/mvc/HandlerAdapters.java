package com.techcourse.support.mvc;

import java.util.ArrayList;
import java.util.List;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.isSupport(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Handler Adapter 를 찾을 수 없습니다."));
    }
}
