package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry(final List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        if (Objects.nonNull(this.handlerMappings)) {
            this.handlerMappings.add(handlerMapping);
        }
    }

    public Optional<Object> getHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            final Object handler = handlerMapping.getHandler(request);
            if (Objects.nonNull(handler)) {
                return Optional.of(handler);
            }
        }
        return Optional.empty();
    }
}
