package com.techcourse;

import java.util.List;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.ControllerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;

public class HandlerAdapters {

    private final List<HandlerAdapter> adapters;

    public HandlerAdapters() {
        this.adapters = List.of(
            new ControllerAdapter(),
            new AnnotationHandlerAdapter()
        );
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return adapters.stream()
            .filter(adapter -> adapter.supports(handler))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("적합한 핸들러 어댑터가 없습니다."));
    }
}
