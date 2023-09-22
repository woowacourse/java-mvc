package webmvc.org.springframework.web.servlet;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappings {

    private final List<HandlerMapping> handler = new ArrayList<>();

    public void add(final HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        handler.add(handlerMapping);
    }

    public Object getHandler(final HttpServletRequest request) {
        return handler.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }
}

