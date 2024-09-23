package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

public class CompositeHandlerMapping implements HandlerMapping {

    private final List<HandlerMapping> handlerMappings;


    public CompositeHandlerMapping(HandlerMapping... handlerMappings) {
        this(List.copyOf(Arrays.asList(handlerMappings)));
    }

    private CompositeHandlerMapping(List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
        initialize();
    }

    @Override
    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    @Override
    public HandlerAdaptor getHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            try {
                return handlerMapping.getHandler(request);
            } catch (Exception ignored) {
            }
        }
        throw new RuntimeException("처리할 수 없는 요청입니다.");
    }
}
