package nextstep.mvc.mapping;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry() {
        this(new ArrayList<>());
    }

    public HandlerMappingRegistry(List<HandlerMapping> handlerMappings) {
        this.handlerMappings = new ArrayList<>(handlerMappings);
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public void initialize() {
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
    }

    public Optional<Object> getHandlerMapping(HttpServletRequest request, Predicate<Object> predicate) {
        return handlerMappings.stream()
                .map(it -> it.getHandler(request))
                .filter(predicate)
                .findAny();
    }
}
