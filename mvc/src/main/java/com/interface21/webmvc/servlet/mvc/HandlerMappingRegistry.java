package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry(List<HandlerMapping> handlerMappings) {
        this.handlerMappings = List.copyOf(handlerMappings);
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(handler -> handler != null)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "요청에 해당하는 핸들러가 없습니다: " + request.getRequestURI()
                ));
    }
}
