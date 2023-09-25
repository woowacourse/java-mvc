package com.techcourse.support.mvc;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;

public class HandlerMappings {

    private final List<HandlerMapping> mappings = new ArrayList<>();

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        mappings.add(handlerMapping);
    }

    public void initialize() {
        mappings.forEach(HandlerMapping::initialize);
    }

    public Object getHandler(HttpServletRequest request) {
        return mappings.stream()
                .filter(mapping -> mapping.canHandle(request))
                .map(mapping -> mapping.getHandler(request))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("요청을 처리할 수 있는 핸들러가 없습니다"));
    }
}
