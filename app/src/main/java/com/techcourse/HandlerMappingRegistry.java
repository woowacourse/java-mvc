package com.techcourse;

import com.interface21.webmvc.servlet.mapping.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public static HandlerMappingRegistry initialize() {
        // todo reflection
        final AnnotationHandlerMapping annotationHandlerMapping = AnnotationHandlerMapping.from("com.techcourse.controller");
        final ManualHandlerMapping manualHandlerMapping = ManualHandlerMapping.initialize();
        return new HandlerMappingRegistry(List.of(annotationHandlerMapping, manualHandlerMapping));
    }

    public Object getHandler(final HttpServletRequest request) {
        final Set<Object> handlers = handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (handlers.isEmpty()) {
            throw new IllegalStateException("요청을 처리할 수 있는 핸들러가 없습니다.");
        }

        if (handlers.size() > 1) {
            throw new IllegalStateException("요청을 처리할 수 있는 핸들러가 2개 이상입니다.");
        }

        return handlers.iterator().next();
    }
}
