package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandlerExecutionStorage {

    private final Map<HandlerKey, HandlerExecution> values = new HashMap<>();

    public void add(final HandlerExecution handlerExecution, final List<HandlerKey> handlerKeys) {
        for (HandlerKey handlerKey : handlerKeys) {
            put(handlerKey, handlerExecution);
        }
    }

    public void addAll(final List<HandlerRegistration> registrations) {
        for (HandlerRegistration registration : registrations) {
            put(registration.handlerKey(), registration.handlerExecution());
        }
    }

    public HandlerExecution get(final HandlerKey handlerKey) {
        return values.get(handlerKey);
    }

    private void put(final HandlerKey handlerKey, final HandlerExecution handlerExecution) {
        final HandlerExecution existing = values.putIfAbsent(handlerKey, handlerExecution);
        if (existing != null) {
            throw new IllegalStateException(
                    "중복된 핸들러 매핑입니다: " + handlerKey + " -> " + existing + ", " + handlerExecution);
        }
    }
}
