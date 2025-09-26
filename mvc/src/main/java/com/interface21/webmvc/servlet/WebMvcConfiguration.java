package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerScanner;

public class WebMvcConfiguration {

    private static final String APPLICATION_PACKAGE = "com.techcourse";

    public static HandlerMappingRegistry handlerMappingRegistry() {
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();

        final var controllerScanner = new ControllerScanner(APPLICATION_PACKAGE);
        final var annotationHandlerMapping = new AnnotationHandlerMapping(controllerScanner);

        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);

        return handlerMappingRegistry;
    }

    public static HandlerAdapterRegistry handlerAdapterRegistry() {
        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

        final var annotationHandlerAdapter = new AnnotationHandlerAdapter();

        handlerAdapterRegistry.addHandlerAdapter(annotationHandlerAdapter);

        return handlerAdapterRegistry;
    }
}
