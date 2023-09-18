package webmvc.org.springframework.web.servlet;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

public class HandlerMappings {

    private final List<HandlerMapping> handler = new ArrayList<>();

    public void add(final HandlerMapping handlerMapping) {
        handler.add(handlerMapping);
    }

    public Object getHandler(final HttpServletRequest request) {
        return handler.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}

