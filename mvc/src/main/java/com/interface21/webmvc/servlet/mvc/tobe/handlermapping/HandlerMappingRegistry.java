package com.interface21.webmvc.servlet.mvc.tobe.handlermapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry() {
        this.handlerMappings = new ArrayList<>();
    }

    public void register(final HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        this.handlerMappings.add(handlerMapping);
    }

    public HandlerMapping getHandlerMapping(final HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            if (handlerMapping.getHandler(request) != null) {
                return handlerMapping;
            }
        }
        throw new IllegalStateException(
                String.format("%s %s 요청에 대한 핸들러를 찾을 수 없습니다.", request.getRequestURI(), request.getMethod()));
    }
}
