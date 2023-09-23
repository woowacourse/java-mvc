package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.HandlerMapping;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class HandlerMappings {

    private final Set<HandlerMapping> handlerMappings;

    public HandlerMappings() {
        this.handlerMappings = new HashSet<>();
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(final HttpServletRequest req) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(req))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("%s %s를 실행시킬 핸들러를 찾을 수 없습니다.", req.getMethod(), req.getRequestURI())));
    }
}
