package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class HandlerMappingRegistry {
    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry() {
        this.handlerMappings = new ArrayList<>();
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Optional<Object> getHandler(final HttpServletRequest request) {
        for (final HandlerMapping handlerMapping : handlerMappings) {
            try {
                Object handler = handlerMapping.getHandler(request);

                if (handler != null) {
                    return Optional.of(handler);
                }

            } catch (Exception e) {
                throw new NoSuchElementException("요청에 대응하는 핸들러가 없습니다.");
            }
        }
        return Optional.empty();
    }
}
