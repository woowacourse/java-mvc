package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class HandlerAdapterRegistry {
    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        Optional<HandlerAdapter> found = handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findAny();

        if (found.isEmpty()) {
            throw new NoSuchElementException("요청을 처리할 핸들러 어댑터가 없습니다.");
        }
        return found.get();
    }
}
