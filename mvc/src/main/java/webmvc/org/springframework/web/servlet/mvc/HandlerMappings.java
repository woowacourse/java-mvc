package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class HandlerMappings {

    private final Set<HandlerMapping> handlerMappings;

    public HandlerMappings(final Set<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
        init();
    }

    private void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Optional<Object> getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst();
    }
}
