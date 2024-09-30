package com.techcourse;

import com.interface21.webmvc.servlet.mvc.tobe.mapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.mapping.HandlerMappings;

public class HandlerMappingsInitializer {

    public HandlerMappingsInitializer() {
    }

    public HandlerMappings initialize() {
        HandlerMappings handlerMappings = new HandlerMappings();
        handlerMappings.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse.controller"));
        handlerMappings.initialize();
        return handlerMappings;
    }
}
