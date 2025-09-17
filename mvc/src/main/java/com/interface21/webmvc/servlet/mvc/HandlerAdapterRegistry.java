package com.interface21.webmvc.servlet.mvc;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {
    private final List<HandlerAdapter> adapters = new ArrayList<>();

    public void register(HandlerAdapter adapter) {
        adapters.add(adapter);
    }

    public HandlerAdapter getAdapter(Object handler) {
        return adapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("처리할 Adapter가 없습니다."));
    }
}
