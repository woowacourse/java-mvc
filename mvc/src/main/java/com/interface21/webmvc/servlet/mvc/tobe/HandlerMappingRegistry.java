package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void register(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public HandlerAdapter getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(handler -> handler != null)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("존재하지 않는 요청입니다."));
    }
}
