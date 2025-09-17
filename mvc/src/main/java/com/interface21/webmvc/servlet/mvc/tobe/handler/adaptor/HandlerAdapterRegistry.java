package com.interface21.webmvc.servlet.mvc.tobe.handler.adaptor;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object object) {
        return handlerAdapters.stream()
            .filter(handlerAdapter -> handlerAdapter.support(object))
            .findFirst()
            .orElseThrow(RuntimeException::new);
    }
}
