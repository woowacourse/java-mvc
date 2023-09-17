package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class HandlerMappings implements HandlerMapping {

    private final List<HandlerMapping> handlers = new ArrayList<>();

    @Override
    public void initialize() {
        handlers.forEach(HandlerMapping::initialize);
    }

    public void addHandler(final HandlerMapping handlerMapping) {
       handlers.add(handlerMapping);
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return handlers.stream()
            .map(requestHandlerMapping -> requestHandlerMapping.getHandler(request))
            .findAny()
            .orElseThrow(HandlerNotFoundException::new);
    }
}
