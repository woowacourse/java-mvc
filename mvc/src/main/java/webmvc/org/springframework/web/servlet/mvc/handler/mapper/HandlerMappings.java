package webmvc.org.springframework.web.servlet.mvc.handler.mapper;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerNotFoundException;

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
