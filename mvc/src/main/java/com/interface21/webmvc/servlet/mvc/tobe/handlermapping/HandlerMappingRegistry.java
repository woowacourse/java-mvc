package com.interface21.webmvc.servlet.mvc.tobe.handlermapping;

import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.annotation.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry() {
        this.handlerMappings = new ArrayList<>();
    }

    public void addMapping(HandlerMapping handlerMapping) {
        this.handlerMappings.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : this.handlerMappings) {
            Object handler = handlerMapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        throw new IllegalArgumentException(String.format("Not found : %s", request.getRequestURI()));
    }
}
