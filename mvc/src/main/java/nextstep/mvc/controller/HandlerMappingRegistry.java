package nextstep.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import nextstep.mvc.HandlerMapping;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry() {
        this.handlerMappings = new ArrayList<>();
    }

    public void add(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Optional<Object> getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findAny();
    }
}
