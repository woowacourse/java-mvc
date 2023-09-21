package nextstep.mvc;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings(final List<HandlerMapping> handlerMappings) {
        this.handlerMappings = new ArrayList<>(handlerMappings);
    }

    public HandlerMappings() {
        this(Collections.emptyList());
    }

    public void initialize() {
        handlerMappings
                .forEach(HandlerMapping::initialize);
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        handlerMappings.add(handlerMapping);
    }

    public Optional<Object> findHandlerMapping(final HttpServletRequest httpServletRequest) {
        return handlerMappings.stream()
                .map(it -> it.getHandler(httpServletRequest))
                .filter(Objects::nonNull)
                .findAny();
    }
}
