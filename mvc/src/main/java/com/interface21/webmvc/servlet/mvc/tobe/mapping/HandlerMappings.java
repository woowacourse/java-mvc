package com.interface21.webmvc.servlet.mvc.tobe.mapping;

import com.interface21.webmvc.servlet.mvc.tobe.HandlingException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings(HandlerMapping... handlerMappings) {
        this.handlerMappings = List.of(handlerMappings);
        initialize();
    }

    private void initialize() {
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(mapping -> mapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new HandlingException("요청을 처리할 핸들러가 존재하지 않습니다."));
    }
}
