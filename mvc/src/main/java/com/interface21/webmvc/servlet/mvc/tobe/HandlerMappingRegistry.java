package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    // 등록된 순서대로 물어보고, 처음으로 핸들러를 찾은 HandlerMapping의 결과를 쓴다
    public Object getHandler(final HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            final Object handler = handlerMapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        throw new HandlerNotFoundException(request.getMethod(), request.getRequestURI());
    }
}
