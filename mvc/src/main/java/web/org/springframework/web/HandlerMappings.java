package web.org.springframework.web;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import webmvc.org.springframework.web.servlet.mvc.handlermapping.HandlerMapping;

public class HandlerMappings {

    private final List<HandlerMapping> mappings = new ArrayList<>();

    void add(HandlerMapping handlerMapping) {
        mappings.add(handlerMapping);
    }

    public Optional<Handler> findHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : mappings) {
            Optional<Handler> handler = handlerMapping.getHandler(request);
            if (handler.isPresent()) {
                return handler;
            }
        }
        return Optional.empty();
    }
}
