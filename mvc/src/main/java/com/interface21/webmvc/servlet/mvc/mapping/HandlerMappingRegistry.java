package com.interface21.webmvc.servlet.mvc.mapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> mappings = new ArrayList<>();

    public void registerMapping(HandlerMapping mapping) {
        mappings.add(mapping);
    }

    public Object getHandler(final HttpServletRequest request) {
        return mappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 요청 URL에 대응하는 handler가 없습니다"));
    }
}
