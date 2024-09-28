package com.techcourse;

import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.techcourse.controller.NotFoundViewController;

public class HandlerManager {

    private final List<HandlerMapping> mappings;

    public HandlerManager(HandlerMapping... handlerMappings) {
        this.mappings = List.of(handlerMappings);
    }

    public void initialize() {
        mappings.forEach(HandlerMapping::initialize);
    }

    public Object findHandler(final HttpServletRequest request) {
        final Optional<HandlerMapping> handlerMapping = mappings.stream()
                .filter(mapping -> mapping.hasHandler(request))
                .findFirst();

        if (handlerMapping.isEmpty()) {
            return new NotFoundViewController();
        }
        return handlerMapping.get().getHandler(request);
    }
}
