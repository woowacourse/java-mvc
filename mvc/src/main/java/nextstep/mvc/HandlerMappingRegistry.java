package nextstep.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    HandlerMappingRegistry() {
        this.handlerMappings = new ArrayList<>();
    }

    void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    Optional<Object> getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
            .peek(HandlerMapping::initialize)
            .map(handlerMapping -> handlerMapping.getHandler(request))
            .filter(Objects::nonNull)
            .findFirst();
    }
}
