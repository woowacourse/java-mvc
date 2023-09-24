package com.techcourse;

import java.util.HashSet;
import java.util.Set;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;

public class HandlerAdapterRegistry {

    private final Set<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry(Set<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = new HashSet<>(handlerAdapters);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(it -> it.support(handler))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("핸들러를 처리할 어댑터가 없습니다."));
    }
}
