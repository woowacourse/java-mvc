package com.techcourse;

import java.util.ArrayList;
import java.util.List;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.ManualHandlerAdapter;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters() {
        this.handlerAdapters = new ArrayList<>();
        handlerAdapters.add(new AnnotationHandlerAdapter());
        handlerAdapters.add(new ManualHandlerAdapter());
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하는 Adapter가 존재하지 않습니다."));
    }
}
