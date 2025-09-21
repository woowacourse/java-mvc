package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.ArrayList;
import java.util.List;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public HandlerMappingRegistry() {
        registerHandlerMapping();
    }

    public List<HandlerMapping> getHandlerMappings() {
        return handlerMappings;
    }

    private void registerHandlerMapping() {
        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.interface21",
                "com.techcourse.controller");
        annotationHandlerMapping.initialize();

        handlerMappings.add(annotationHandlerMapping);
    }
}
