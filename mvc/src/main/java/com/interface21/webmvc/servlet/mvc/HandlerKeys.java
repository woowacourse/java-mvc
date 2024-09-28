package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.mvc.tobe.DuplicateHandlerKeyException;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;

import java.util.HashMap;
import java.util.Map;

public record HandlerKeys(Map<HandlerKey, HandlerExecution> values) {
    public HandlerKeys() {
        this(new HashMap<>());
    }

    public void put(final HandlerKey handlerKey, final HandlerExecution handlerExecution) {
        if (values.containsKey(handlerKey)) {
            throw new DuplicateHandlerKeyException(handlerKey);
        }
        values.put(handlerKey, handlerExecution);
    }
    public HandlerExecution get(final HandlerKey handlerKey) {
        return values.get(handlerKey);
    }
}
