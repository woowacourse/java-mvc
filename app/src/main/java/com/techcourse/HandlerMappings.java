package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

public class HandlerMappings {
    private static final Object[] BASE_PACKAGE_PATH = {"com.techcourse"};

    private final List<HandlerMapping> mappings;

    private HandlerMappings(final List<HandlerMapping> mappings) {
        this.mappings = mappings;
    }

    public static HandlerMappings create() {
        return new HandlerMappings(initHandlerMappings());
    }

    private static List<HandlerMapping> initHandlerMappings() {
        return HandlerMappingsFactory.createHandlerMappings(BASE_PACKAGE_PATH)
                .stream()
                .peek(HandlerMapping::initialize)
                .collect(Collectors.toUnmodifiableList());
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        return this.mappings.stream()
                .filter(handlerMapping -> handlerMapping.isMatch(request))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("해당하는 HandlerMapping을 찾을 수 없습니다."))
                .getHandler(request);
    }
}
