package com.techcourse.handler;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import com.techcourse.HandlerAdapter;

public class HandlerAdapterRegistry {

    public final List<HandlerAdapter> registry;

    public HandlerAdapterRegistry() {
        this.registry = new ArrayList<>();
    }

    public int addAdapter(final HandlerAdapter handlerAdapter) {
        registry.add(handlerAdapter);
        return registry.size();
    }

    public HandlerAdapter get(final HttpServletRequest request) {
        return registry.stream()
                .filter(handlerMappingAdapter -> handlerMappingAdapter.support(request))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("핸들러를 찾을 수 없는 HTTP request 요청"));
    }
}
