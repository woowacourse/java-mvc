package com.techcourse.servlet.handler.mapper;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings(List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    public static HandlerMappings defaultHandlerMappings() {
        List<HandlerMapping> handlerMappings = List.of(
                new AnnotationHandlerMapping("com.techcourse.controller.annotation"),
                new ManualHandlerMapping()
        );
        return new HandlerMappings(handlerMappings);
    }

    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.hasHandler(request))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("request를 처리한 핸들러가 존재하지 않습니다"))
                .getHandler(request);
    }
}
