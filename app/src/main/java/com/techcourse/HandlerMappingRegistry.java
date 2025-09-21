package com.techcourse;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandlerMappingRegistry {
    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry() {
        this.handlerMappings = new ArrayList<>();
    }

    void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    Optional<HandlerMapping> getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .filter(handlerMapping ->
                        handlerMapping.getHandler(request) != null
                ).findFirst();
    }
}
