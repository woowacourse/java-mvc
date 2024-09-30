package com.interface21.webmvc.servlet.mvc.handler.mapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

public class HandlerMappings {
    private final List<HandlerMapping> mappings;

    public HandlerMappings(List<HandlerMapping> mappings) {
        this.mappings = List.copyOf(mappings);
    }

    public Object getHandler(HttpServletRequest request) {
        return mappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format(
                        "%s %s를 처리할 수 있는 핸들러가 존재하지 않습니다.", request.getMethod(), request.getRequestURI())));
    }

    public void init() {
        mappings.forEach(HandlerMapping::init);
    }
}
