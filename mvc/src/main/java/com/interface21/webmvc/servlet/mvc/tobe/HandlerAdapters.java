package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class HandlerAdapters {

    private final Map<Class<?>, HandlerAdapter<?>> adapters;

    public HandlerAdapters(HandlerAdapter<?>... handlerAdapters) {
        this.adapters = Arrays.stream(handlerAdapters)
                .collect(Collectors.toMap(
                        HandlerAdapter::getSupportedClass,
                        adapter -> adapter,
                        (a, b) -> { throw new HandlingException("같은 종류의 핸들러를 처리하는 어댑터를 중복 등록할 수 없습니다."); }
                ));
    }

    public HandlerAdapter<?> getAdapter(Object handler) {
        return adapters.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isAssignableFrom(handler.getClass()))
                .map(Entry::getValue)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 핸들러를 처리할 수 있는 어댑터가 존재하지 않습니다."));
    }
}
