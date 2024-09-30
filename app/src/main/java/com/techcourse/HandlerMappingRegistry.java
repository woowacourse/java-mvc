package com.techcourse;

import com.interface21.webmvc.servlet.mvc.exception.NonExistenceHandlerException;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        if (!handlerMappings.contains(handlerMapping)) {
            handlerMappings.add(handlerMapping);
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) throws NonExistenceHandlerException {
        return (HandlerExecution) handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() ->
                        new NonExistenceHandlerException(request.getMethod() + "-" + request.getRequestURI()));
    }
}
