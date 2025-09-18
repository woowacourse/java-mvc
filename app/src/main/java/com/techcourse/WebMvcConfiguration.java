package com.techcourse;

import com.interface21.webmvc.servlet.mvc.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerScanner;

public class WebMvcConfiguration {

    private static final String APPLICATION_PACKAGE = "com.techcourse";

    public static HandlerMappingRegistry handlerMappingRegistry() {
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();

        final var controllerHandlerMappingInitializer = new ControllerHandlerMappingInitializer();
        final var controllerHandlerMapping = controllerHandlerMappingInitializer.handle();

        final var controllerScanner = new ControllerScanner(APPLICATION_PACKAGE);
        final var annotationHandlerMapping = new AnnotationHandlerMapping(controllerScanner);

        handlerMappingRegistry.addHandlerMapping(controllerHandlerMapping);
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);

        return handlerMappingRegistry;
    }

    public static HandlerAdapterRegistry handlerAdapterRegistry() {
        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

        final var controllerHandlerAdapter = new ControllerHandlerAdapter();
        final var annotationHandlerAdapter = new AnnotationHandlerAdapter();

        handlerAdapterRegistry.addHandlerAdapter(controllerHandlerAdapter);
        handlerAdapterRegistry.addHandlerAdapter(annotationHandlerAdapter);

        return handlerAdapterRegistry;
    }
}
