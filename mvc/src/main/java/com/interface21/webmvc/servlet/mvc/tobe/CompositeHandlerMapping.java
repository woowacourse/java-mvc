package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public class CompositeHandlerMapping implements HandlerMapping {

    private final List<HandlerMapping> handlerMappings;

    public CompositeHandlerMapping(List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            Object handler = handlerMapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        String invalidRequestInfo = String.join(" ", request.getMethod(), request.getRequestURI());
        throw new IllegalArgumentException("해당 요청을 처리할 수 있는 핸들러가 존재하지 않습니다. : " + invalidRequestInfo);
    }
}
