package com.interface21.webmvc.servlet;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdapter;

class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters() {
        List<HandlerAdapter> adapters = new ArrayList<>();
        adapters.add(new HandlerExecutionAdapter());
        adapters.add(new ControllerHandlerAdapter());

        this.handlerAdapters = adapters;
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("핸들러 어댑터가 존재하지 않습니다. " + handler.toString()));
    }
}
