package com.techcourse;

import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void initialize() {
        addHandlerAdapter(new AnnotationHandlerAdapter());
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        this.handlerAdapters.add(handlerAdapter);
    }

    public Optional<HandlerAdapter> getHandlerAdapter(final Object handler) {
        for (final HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.supports(handler)) {
                return Optional.of(handlerAdapter);
            }
        }
        return Optional.empty();
    }
}
