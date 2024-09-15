package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.Map;

public class HandlerExecutions {
    
    private final Map<HandlerKey, HandlerExecution> values;

    public HandlerExecutions(Map<HandlerKey, HandlerExecution> values) {
        this.values = values;
    }
    
    public HandlerExecution getHandlerExecution(HandlerKey handlerKey) {
        return values.get(handlerKey);
    }
}
