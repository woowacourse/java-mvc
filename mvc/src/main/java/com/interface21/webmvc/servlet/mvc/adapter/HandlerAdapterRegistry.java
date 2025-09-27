package com.interface21.webmvc.servlet.mvc.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> adapters = new ArrayList<>();

    public void registerAdapter(HandlerAdapter adapter) {
        adapters.add(adapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return adapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당하는 handler를 처리할 adapter를 찾지 못했습니다"));
    }
}
