package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandlerMappings {

    private final List<HandlerMapping> mappings = new ArrayList<>();

    public Object getHandler(HttpServletRequest request) {
        return mappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .flatMap(Optional::stream)
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("요청을 처리할 핸들러가 존재하지 않습니다."));
    }

    public void init() {
        mappings.forEach(HandlerMapping::initialize);
    }

    public void add(HandlerMapping handlerMapping) {
        mappings.add(handlerMapping);
    }
}
