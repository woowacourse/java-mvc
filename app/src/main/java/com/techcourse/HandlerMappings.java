package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappings {

    private List<HandlerMapping> mappings;

    public void init() {
        mappings = new ArrayList<>();
        mappings.add(new ManualHandlerMapping());
        mappings.add(new RequestMappingHandlerMapping("com"));
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        return mappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(HandlerMappingNotFoundException::new);
    }
}
