package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record HandlerMappingRegistry(List<HandlerMapping> handlerMappings) {

    private static final Logger log = LoggerFactory.getLogger(HandlerMappingRegistry.class);

    public HandlerMappingRegistry() {
        this(new ArrayList<>());
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        this.handlerMappings.add(handlerMapping);
    }

    public Optional<Object> getHandler(final HttpServletRequest req) {
        final String requestURI = req.getRequestURI();
        log.debug("Method : {}, Request URI : {}", req.getMethod(), requestURI);

        final var handlerMapping = handlerMappings.stream()
                .filter(hm -> hm.getHandler(req).isPresent())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No handler for request URI: " + requestURI));
        return handlerMapping.getHandler(req);
    }
}
