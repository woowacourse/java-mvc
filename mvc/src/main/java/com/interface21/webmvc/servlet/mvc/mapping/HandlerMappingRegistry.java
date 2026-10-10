package com.interface21.webmvc.servlet.mvc.mapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class HandlerMappingRegistry {
    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry(List<HandlerMapping> handlerMappings) {
        this.handlerMappings = List.copyOf(handlerMappings);
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream().map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("요청에 매핑된 핸들러가 없습니다: " + request.getRequestURI()));
    }

    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }
}
