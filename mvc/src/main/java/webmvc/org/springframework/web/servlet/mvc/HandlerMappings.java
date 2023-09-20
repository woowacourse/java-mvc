package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class HandlerMappings {

    private final Set<HandlerMapping> handlerMappings;

    public HandlerMappings() {
        this(new HashSet<>());
    }

    public HandlerMappings(final Set<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        this.handlerMappings.add(handlerMapping);
    }

    public Optional<Object> getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst();
    }
}
