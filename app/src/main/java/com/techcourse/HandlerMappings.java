package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.tobe.DefaultHandler;
import webmvc.org.springframework.web.servlet.mvc.tobe.Handler;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

import java.util.List;
import java.util.Objects;

public class HandlerMappings {
    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings(HandlerMapping... handlerMappings) {
        this.handlerMappings = List.of(handlerMappings);
    }

    public void initialize() throws Exception {
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
    }

    public Handler getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .filter(Handler::isSupport)
                .findFirst()
                .orElse(new DefaultHandler());
    }
}
