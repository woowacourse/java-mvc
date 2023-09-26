package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings;
    private HandlerMapping notFoundHandlerMapping;

    public HandlerMappings() {
        this.handlerMappings = new ArrayList<>();
    }

    public void add(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public void addNotFoundHandlerMapping(final HandlerMapping notFoundHandlerMapping) {
        this.notFoundHandlerMapping = notFoundHandlerMapping;
    }

    public void initialize() {
        for (final HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerMapping handlerMapping = handlerMappings.stream()
                .filter(h -> h.isSupport(request))
                .findAny()
                .orElse(notFoundHandlerMapping);
        if (handlerMapping == null) {
            throw new RuntimeException("Could not find supporting hanlder for " + request.getMethod() + " " + request.getRequestURI());
        }
        return handlerMapping.getHandler(request);
    }
}
