package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void add(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest request) {
        final HandlerMapping handlerMapping = handlerMappings.stream()
                .filter(handler -> handler.getHandler(request) != null)
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("핸들러가 존재하지 않습니다."));
        return handlerMapping.getHandler(request);
    }
}
