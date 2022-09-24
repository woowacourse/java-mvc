package nextstep.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry() {
        handlerMappings = new ArrayList<>();
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public void init() {
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
    }

    public Optional<Object> getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
            .map(handlerMapping -> handlerMapping.getHandler(request))
            .filter(Objects::nonNull)
            .findFirst();
    }

}
