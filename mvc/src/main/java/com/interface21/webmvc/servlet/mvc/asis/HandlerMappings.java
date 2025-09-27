package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings(final List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    public Object getHandler(HttpServletRequest request) {
        for (HandlerMapping mapping : handlerMappings) {
            Object handler = mapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }
}
