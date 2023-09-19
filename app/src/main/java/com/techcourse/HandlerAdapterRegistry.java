package com.techcourse;

import java.util.HashSet;
import java.util.Set;
import webmvc.org.springframework.web.servlet.mvc.asis.ControllerHandlerAdaptor;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutionHandlerAdaptor;

public class HandlerAdapterRegistry {

    private final Set<HandlerAdapter> handlerAdapters = new HashSet<>();

    public HandlerAdapterRegistry() {
        handlerAdapters.add(new ControllerHandlerAdaptor());
        handlerAdapters.add(new HandlerExecutionHandlerAdaptor());
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(it -> it.support(handler))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("핸들러를 처리할 어댑터가 없습니다."));
    }
}
