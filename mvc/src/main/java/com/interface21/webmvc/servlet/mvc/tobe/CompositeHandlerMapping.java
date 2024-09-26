package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CompositeHandlerMapping implements HandlerMapping {

    private final List<HandlerMapping> handlerMappings;

    public CompositeHandlerMapping(HandlerMapping... handlerMappings) {
        this.handlerMappings = List.copyOf(Arrays.asList(handlerMappings));
        initialize();
    }

    @Override
    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    @Override
    public HandlerAdaptor getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> getHandler(handlerMapping, request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("처리할 수 없는 요청입니다."));
    }

    private HandlerAdaptor getHandler(HandlerMapping handlerMapping, HttpServletRequest request) {
        try {
            return handlerMapping.getHandler(request);
        } catch (Exception e) {
            return null;
        }
    }
}
