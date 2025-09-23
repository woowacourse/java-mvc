package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.mvc.handler.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.NotFoundHandler;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        this.handlerMappings.add(handlerMapping);
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(new NotFoundHandler());
    }
}
