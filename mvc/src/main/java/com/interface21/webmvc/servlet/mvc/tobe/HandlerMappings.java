package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.ArrayList;
import java.util.List;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;

public class HandlerMappings {

    private final List<HandlerMapping> mappings;

    public HandlerMappings(HandlerMapping... handlerMappings) {
        this.mappings = new ArrayList<>(List.of(handlerMappings));
    }

    public void initialize() {
        mappings.forEach(HandlerMapping::initialize);
    }

    @Nullable
    public Object getHandler(HttpServletRequest request) throws Exception {
        for (HandlerMapping mapping : mappings) {
            Object handler = mapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }
}
