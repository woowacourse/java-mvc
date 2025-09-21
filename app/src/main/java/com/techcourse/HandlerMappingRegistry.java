package com.techcourse;

import com.interface21.webmvc.servlet.mvc.handler.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Optional<Object> getHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            Optional<Object> handler = handlerMapping.getHandler(request);
            if (handler.isPresent()) {
                return handler;
            }
        }
        return Optional.empty();
    }
}
