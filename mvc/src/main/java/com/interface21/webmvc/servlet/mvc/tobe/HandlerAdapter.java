package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public class HandlerAdapter {

    private final List<HandlerMapping> handlerMappings;

    public HandlerAdapter(final List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
    }

    public Object findHandler(final HttpServletRequest request) {
        Optional<HandlerMapping> handlerMapping = handlerMappings.stream()
                .filter(mapping -> mapping.getHandler(request) != null)
                .findFirst();
        return handlerMapping.map(mapping -> mapping.getHandler(request))
                .orElse(null);
    }
}
