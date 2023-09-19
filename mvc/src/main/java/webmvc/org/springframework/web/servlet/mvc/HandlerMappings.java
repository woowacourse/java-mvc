package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings() {
        this.handlerMappings = new ArrayList<>();
    }

    public void add(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
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
                .orElseThrow(() -> new RuntimeException("Could not find supporting hanlder for " + request.getMethod() + " " + request.getRequestURI()));
        return handlerMapping.getHandler(request);
    }
}
