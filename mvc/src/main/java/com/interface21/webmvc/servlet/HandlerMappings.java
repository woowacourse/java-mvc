package com.interface21.webmvc.servlet;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import jakarta.servlet.http.HttpServletRequest;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings(String indexViewName, String basePackage) {
        List<HandlerMapping> mappings = new ArrayList<>();
        mappings.add(new ControllerHandlerMapping(indexViewName, basePackage));
        mappings.add(new AnnotationHandlerMapping(basePackage));

        this.handlerMappings = mappings;
        initialize();
    }

    private void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(mapping -> mapping.getHandler(request))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("핸들러가 존재하지 않습니다. " + request.getRequestURI()));
    }
}
