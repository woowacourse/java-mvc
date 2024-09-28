package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class HandlerMappingRegistry {

    List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void addHandlerMappings(HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }
}
