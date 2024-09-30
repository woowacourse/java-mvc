package com.interface21.webmvc.servlet.mvc.tobe.registry;

import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class HandlerAdapterRegistry {

    private static final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    private HandlerAdapterRegistry() {
    }

    public static void addHandlerAdapters(Set<HandlerAdapter> initializedHandlerAdapters) {
        initializedHandlerAdapters.stream()
                .filter(HandlerAdapterRegistry::isNotContainedHandler)
                .forEach(handlerAdapters::add);
    }

    private static boolean isNotContainedHandler(HandlerAdapter handlerAdapter) {
        return !handlerAdapters.contains(handlerAdapter);
    }

    public static Optional<HandlerAdapter> getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findAny();
    }
}
