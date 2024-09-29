package com.interface21.webmvc.servlet.mvc.tobe.handler;

import java.util.HashMap;
import java.util.Map;

public class HandlerExecutions {

    private final Map<HandlerKey, HandlerExecution> values;

    public HandlerExecutions() {
        this.values = new HashMap<>();
    }

    public void add(final HandlerKey key, final HandlerExecution execution) {
        values.put(key, execution);
    }

    public HandlerExecution findHandlerExecution(final HandlerKey key) {
        if (!values.containsKey(key)) {
            return null;
        }

        return values.get(key);
    }
}
