package com.techcourse;

import java.util.ArrayList;
import java.util.List;

import webmvc.org.springframework.web.servlet.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerAdapter;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public HandlerAdapters() {
        handlerAdapters.add(new ManulHandlerAdapter());
        handlerAdapters.add(new AnnotationHandlerAdapter());
    }

    public void add(HandlerAdapter handlerAdapter) {
        this.handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

}
