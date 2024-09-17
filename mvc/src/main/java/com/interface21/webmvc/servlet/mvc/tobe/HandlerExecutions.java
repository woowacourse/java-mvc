package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class HandlerExecutions {

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();

    public void save(HandlerKey handlerKey, HandlerExecution handlerExecution) {
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    public HandlerExecution find(HandlerKey handlerKey) {
        if (handlerExecutions.containsKey(handlerKey)) {
            return handlerExecutions.get(handlerKey);
        }
        throw new NoSuchElementException("핸들러가 존재하지 않습니다.");
    }
}
