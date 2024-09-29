package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest request) {
        try {
            return handlerMappings.stream()
                    .filter(handlerMapping -> handlerMapping.supports(request))
                    .map(handlerMapping -> handlerMapping.getHandler(request))
                    .findAny()
                    .orElseThrow(NullPointerException::new);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(String.format("Can not find proper handler from requestUrl: %s, requestMethod: %s", request.getRequestURI(), request.getMethod()));
        }
    }
}
