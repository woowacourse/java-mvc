package com.techcourse;

import com.interface21.webmvc.servlet.mvc.handler.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            Optional<Object> handler = handlerMapping.getHandler(request);
            if (handler.isPresent()) {
                return handler;
            }
        }
        throw new IllegalArgumentException("요청을 처리할 수 있는 핸들러가 존재하지 않습니다.");
    }
}
