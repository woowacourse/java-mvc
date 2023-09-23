package com.techcourse;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerKey;

public class HandlerAdaptor<T> {
    private final T handler;
    private final Set<HandlerKey> handlerKeys;

    private HandlerAdaptor(T handler, Set<HandlerKey> handlerKeys) {
        this.handler = handler;
        this.handlerKeys = handlerKeys;
    }

    public static <T> HandlerAdaptor<T> of(T handler) throws NoSuchFieldException, IllegalAccessException {
        final Field field = handler.getClass().getDeclaredField("controllers");
        field.setAccessible(true);
        final Set<HandlerKey> handlerKeys = getHandlerKeys((Map<?, ?>) field.get(handler));
        return new HandlerAdaptor(handler, handlerKeys);
    }

    private static Set<HandlerKey> getHandlerKeys(Map<?, ?> handlerKeys) {
        if (handlerKeys.isEmpty()) {
            return Set.of();
        }
        if (handlerKeys.get(0) instanceof String) {
            return handlerKeys.keySet().stream()
                    .map(url -> new HandlerKey((String) url, RequestMethod.GET))
                    .collect(Collectors.toSet());
        }
        return (Set<HandlerKey>) handlerKeys.keySet();
    }

    public boolean isHandle(HandlerKey handlerKey) {
        return this.handlerKeys.contains(handlerKey);
    }

    public T getHandlerMapping() {
        return this.handler;
    }
}
