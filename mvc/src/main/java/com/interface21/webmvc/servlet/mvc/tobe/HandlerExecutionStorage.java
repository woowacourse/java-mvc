package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandlerExecutionStorage {

    private final Map<HandlerKey, HandlerExecution> values = new HashMap<>();

    public void add(final HandlerExecution handlerExecution, final RequestMapping requestMapping) {
        List<HandlerKey> handlerKeys = new RequestMappingInfo(requestMapping).handlerKeys();

        for (HandlerKey handlerKey : handlerKeys) {
            values.put(handlerKey, handlerExecution);
        }
    }

    public HandlerExecution get(final HandlerKey handlerKey) {
        return values.get(handlerKey);
    }
}
