package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerMappingNotFoundException;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.Request;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdaptor {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public HandlerAdaptor() {
    }

    public void initialize() {
        handlerMappings.add(new AnnotationHandlerMapping());
        handlerMappings.add(new ManualHandlerMapping());
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Object getHandler(final HttpServletRequest httpServletRequest) {
        final Request request = new Request(
                httpServletRequest.getRequestURI(),
                httpServletRequest.getMethod()
        );
        final HandlerMapping handlerMapping = findHandler(request);
        return handlerMapping.getHandler(request);
    }

    private HandlerMapping findHandler(final Request request) {
        return handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.hasMapping(request))
                .findFirst()
                .orElseThrow(HandlerMappingNotFoundException::new);
    }
}
