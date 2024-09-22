package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class HandlerMappings {

    private static final int UNIQUE_SIZE = 1;

    private final Set<HandlerMapping> handlerMappings;

    public HandlerMappings(HandlerMapping... handlerMappings) {
        this.handlerMappings = Set.of(handlerMappings);
    }

    public void initialize() {
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
    }

    public Object getHandler(HttpServletRequest request) {
        Set<Object> handlers = handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        validateHandlers(handlers);
        return handlers.stream()
                .findAny()
                .orElseThrow(() -> new HandlingException("요청을 처리할 핸들러가 존재하지 않습니다."));
    }

    private void validateHandlers(Set<Object> handlers) {
        if(handlers.isEmpty()) {
            throw new HandlingException("요청을 처리할 핸들러가 존재하지 않습니다.");
        }
        if(handlers.size() != UNIQUE_SIZE) {
            throw new HandlingException("요청을 처리할 핸들러가 두 개 이상 존재합니다.");
        }
    }
}
