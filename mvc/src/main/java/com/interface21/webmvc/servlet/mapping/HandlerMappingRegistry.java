package com.interface21.webmvc.servlet.mapping;

import com.interface21.webmvc.servlet.Handler;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public static HandlerMappingRegistry initialize(
            final String basePackage,
            final ControllerMapping controllerMapping
    ) {
        // todo reflection
        final HandlerExecutionMapping handlerExecutionMapping = HandlerExecutionMapping.from(basePackage);
        return new HandlerMappingRegistry(List.of(handlerExecutionMapping, controllerMapping));
    }

    public Handler getHandler(final HttpServletRequest request) {
        final Set<Handler> handlers = handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(handler -> handler.instance() != null)
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
