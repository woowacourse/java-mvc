package com.interface21.webmvc.servlet.mvc;

import com.interface21.web.bind.annotation.RequestMethod;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandlerRegistry {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void registerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public void registerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public Optional<Object> getHandler(final String path, final RequestMethod requestMethod) {
        Object handler = null;
        for (HandlerMapping handlerMapping : handlerMappings) {
            handler = handlerMapping.getHandler(path, requestMethod);
            if (handler != null) {
                return Optional.of(handler);
            }
        }
        return Optional.empty();
    }

    public Optional<HandlerAdapter> getAdapter(final Object handler) {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.supports(handler)) {
                return Optional.of(handlerAdapter);
            }
        }
        return Optional.empty();
    }
}
