package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

public class HandlerMappingComposite {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingComposite(final List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    public Optional<HandlerMapping> getHandlerMapping(final HttpServletRequest request) {
        return handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.support(request))
                .findFirst();
    }
}
