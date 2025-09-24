package com.interface21.webmvc.servlet.mvc.tobe.mapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class HandlerMappingRegistry {

    List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
        handlerMappings.sort(Comparator.comparingInt(HandlerMapping::getOrder));
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(mapping -> mapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(getNoHandlerFoundExceptionSupplier(request));
    }

    private Supplier<NoHandlerFoundException> getNoHandlerFoundExceptionSupplier(HttpServletRequest request) {
        return () -> new NoHandlerFoundException(
                String.format("요청하신 API를 처리할 핸들러를 찾을 수 없습니다. [HTTP Method=%s, URI=%s]",
                        request.getMethod(),
                        request.getRequestURI()
                ));
    }
}
