package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

public final class HandlerRegistry {

    private final Map<HandlerKey, HandlerExecution> handlers;

    public HandlerRegistry() {
        this(Map.of());
    }

    private HandlerRegistry(final Map<HandlerKey, HandlerExecution> handlers) {
        this.handlers = Map.copyOf(handlers);
    }

    public HandlerRegistry register(final HandlerKey key, final HandlerExecution execution) {
        if (handlers.containsKey(key)) {
            throw new IllegalStateException("Duplicate handler mapping: " + key);
        }
        final var registeredHandlers = new HashMap<>(handlers);
        registeredHandlers.put(key, execution);
        return new HandlerRegistry(registeredHandlers);
    }

    public HandlerExecution getHandler(final String path, final RequestMethod requestMethod) {
        if (requestMethod != null) {
            final var execution = handlers.get(new HandlerKey(path, requestMethod));
            if (execution != null) {
                return execution;
            }
        }
        return handlers.get(HandlerKey.anyMethod(path));
    }
}
