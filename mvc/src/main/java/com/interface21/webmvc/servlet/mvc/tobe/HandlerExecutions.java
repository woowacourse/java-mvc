package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HandlerExecutions {

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public HandlerExecutions() {
        this.handlerExecutions = new HashMap<>();
    }

    public void put(RequestMapping requestMapping, HandlerExecution handlerExecution) {
        String uri = requestMapping.value();

        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        Arrays.stream(requestMethods)
                .forEach(requestMethod -> handlerExecutions.put(new HandlerKey(uri, requestMethod), handlerExecution));
    }

    public HandlerExecution get(HandlerKey handlerKey) {
        return handlerExecutions.get(handlerKey);
    }
}
