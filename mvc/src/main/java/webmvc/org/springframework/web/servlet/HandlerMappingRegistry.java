package webmvc.org.springframework.web.servlet;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;


    public HandlerMappingRegistry() {
        this.handlerMappings = new ArrayList<>();
    }

    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    Optional<Object> getHandler(final HttpServletRequest httpServletRequest) {
        for (final HandlerMapping handlerMapping : handlerMappings) {
            if (handlerMapping.getHandler(httpServletRequest) != null) {
                return Optional.of(handlerMapping.getHandler(httpServletRequest));
            }
        }
        return Optional.empty();
    }
}
