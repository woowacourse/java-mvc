package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public HandlerAdapterRegistry() {
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();
        ErrorHandlerAdapter errorHandlerAdapter = new ErrorHandlerAdapter();
        this.handlerAdapters.addAll(List.of(annotationHandlerAdapter, errorHandlerAdapter));
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("핸들러를 처리할 핸들러 어댑터가 존재하지 않습니다 " + handler));
    }
}
