package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.servlet.HandlerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class HandlerAdaptorRegistry {

    List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void addHandlerAdapters(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapters(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }
}
