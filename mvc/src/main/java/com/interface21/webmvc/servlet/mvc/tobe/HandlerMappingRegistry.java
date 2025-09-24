package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void addHandler(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    // 처리할 수 있는 핸들러(Controller or Execution Handler) 찾기
    public Object getHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            Object handler = handlerMapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        throw new IllegalStateException("처리할 수 있는 핸들러가 없습니다.");
    }

    public void initialize() {
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
    }
}
