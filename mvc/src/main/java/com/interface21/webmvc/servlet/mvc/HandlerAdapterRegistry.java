package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.mvc.handler.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.NotFoundHandlerAdapter;
import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        this.handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.canHandle(handler))
                .findFirst()
                .orElse(new NotFoundHandlerAdapter());
    }
}
