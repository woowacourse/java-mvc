package webmvc.org.springframework.web.servlet.mvc.tobe.handler.mapper;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.exception.HandlerMapperNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappers {

    private final List<HandlerMapper> handlerMappings;

    public HandlerMappers() {
        this.handlerMappings = new ArrayList<>();
    }

    public void init() {
        handlerMappings.forEach(HandlerMapper::initialize);
    }

    public void addHandlerMapper(final HandlerMapper handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Object findHandlerMapper(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new HandlerMapperNotFoundException(request.getMethod(), request.getRequestURI()));
    }
}
