package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.annotation.AnnotationHandlerAdapter;
import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public HandlerAdapterRegistry() {
    }

    public void initialize() {
        handlerAdapters.add(new AnnotationHandlerAdapter());
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.support(handler))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 핸들러를 수행하는 어댑터가 없습니다."));
    }
}
