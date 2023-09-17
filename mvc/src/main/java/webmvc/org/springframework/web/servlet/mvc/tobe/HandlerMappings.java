package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerNotFoundException;

import java.util.List;

public class HandlerMappings implements HandlerMapping {

    private final List<HandlerMapping> handlers;

    public HandlerMappings(List<HandlerMapping> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void initialize() {
        handlers.forEach(HandlerMapping::initialize);
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return handlers.stream()
            .filter(requestHandlerMapping -> requestHandlerMapping.getHandler(request) != null)
            .map(requestHandlerMapping -> requestHandlerMapping.getHandler(request))
            .findFirst()
            .orElseThrow(HandlerNotFoundException::new);
    }
}
