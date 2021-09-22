package nextstep.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;

public class HandlerMappingRegistry {
    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry() {
        handlerMappings = new ArrayList<>();
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                              .map(handlerMapping -> handlerMapping.getHandler(request))
                              .filter(Objects::nonNull)
                              .findFirst()
                              .orElseThrow(() -> new NoSuchElementException("there's no handler mapping"));
    }

    public void initialize() {
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
    }
}
