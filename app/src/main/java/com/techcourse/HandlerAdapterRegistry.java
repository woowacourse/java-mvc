package com.techcourse;

import web.org.springframework.web.exception.InvalidHandlerTypeException;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;

import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry(List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new InvalidHandlerTypeException("해당하는 핸들러 어댑터가 없습니다."));
    }
}
