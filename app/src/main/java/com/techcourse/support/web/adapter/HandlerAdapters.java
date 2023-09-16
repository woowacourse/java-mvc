package com.techcourse.support.web.adapter;

import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapters {
    private final List<HandlerAdapter> adapters;

    public HandlerAdapters() {
        this.adapters = new ArrayList<>();
    }

    public void init() {
        adapters.add(new ManualHanlderAdapter());
        adapters.add(new AnnotationHandlerAdapter());
    }

    public HandlerAdapter getAdapter(final Object handler) {
        for (final HandlerAdapter handlerAdapter : adapters) {
            if (handlerAdapter.supports(handler)) {
                return handlerAdapter;
            }
        }
        return null;
    }
}
