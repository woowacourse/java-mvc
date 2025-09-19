package com.techcourse;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.canHandle(handler))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 핸들러를 처리할 수 있는 핸들러 어댑터가 없습니다. : " + handler));
    }
}
