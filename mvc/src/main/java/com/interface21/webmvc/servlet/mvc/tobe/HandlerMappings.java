package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void addHandlerMapping(HandlerMapping handlerMapping)  {
        handlerMappings.add(handlerMapping);
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        if (handlerMappings.isEmpty()) {
            return null;
        }

        Object handler = handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        return (HandlerExecution) handler;
    }
}
