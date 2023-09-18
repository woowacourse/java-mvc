package com.techcourse.support.web.handler.mapping;

import com.techcourse.support.web.handler.adaptor.ManualHandlerMappingWrapped;
import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import java.util.List;
import java.util.Objects;

public class HandlerMappings {

    private final List<HandlerMapping> values = List.of(new ManualHandlerMappingWrapped(),
            new AnnotationHandlerMapping("com.techcourse"));

    public void initialize() {
        for (final HandlerMapping handlerMapping : values) {
            handlerMapping.initialize();
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        return values.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
}
