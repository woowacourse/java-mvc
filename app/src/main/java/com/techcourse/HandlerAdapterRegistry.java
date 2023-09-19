package com.techcourse;

import java.util.ArrayList;
import java.util.List;
import webmvc.org.springframework.web.servlet.mvc.asis.ControllerHandlerAdaptor;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutionHandlerAdaptor;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public HandlerAdapterRegistry() {
        handlerAdapters.add(new ControllerHandlerAdaptor());
        handlerAdapters.add(new HandlerExecutionHandlerAdaptor());
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        if (handlerAdapters.contains(handlerAdapter)) {
            throw new RuntimeException("이미 핸들러 어댑터가 존재합니다.");
        }
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(it -> it.support(handler))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("핸들러를 처리할 어댑터가 없습니다."));
    }
}
