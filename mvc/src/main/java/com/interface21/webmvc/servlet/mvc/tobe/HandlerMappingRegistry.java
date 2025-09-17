package com.interface21.webmvc.servlet.mvc.tobe;

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

    public Object getHandler(final HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            if (handlerMapping.getHandler(request) != null) {
                return handlerMapping.getHandler(request);
            }
        }
        throw new IllegalStateException("요청에 대한 핸들러를 찾을 수 없습니다.");
    }
}
