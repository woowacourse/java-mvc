package com.interface21.webmvc.servlet.mvc.tobe.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HandlerExecutions {

    private final Map<HandlerKey, HandlerExecution> values;

    public HandlerExecutions() {
        this.values = new HashMap<>();
    }

    public void add(final HandlerKey key, final HandlerExecution execution) {
        values.put(key, execution);
    }

    public Optional<HandlerExecution> findHandlerExecution(final HandlerKey key) {
        if (!values.containsKey(key)) {
            return Optional.empty();
        }

        return Optional.of(values.get(key));
    }
}
