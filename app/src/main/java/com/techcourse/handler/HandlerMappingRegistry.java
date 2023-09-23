package com.techcourse.handler;

import com.techcourse.controller.NotFoundController;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void init() {
        handlerMappings.add(new ControllerHandlerMapping());
        handlerMappings.add(new AnnotationHandlerMapping("com.techcourse.controller"));

        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
    }

    public Object findHandler(HttpServletRequest httpServletRequest) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(httpServletRequest))
                .filter(Objects::nonNull)
                .findAny()
                .orElse(new NotFoundController());
    }
}
