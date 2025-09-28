package com.interface21.webmvc.servlet.mvc.tobe.dispatcherservlet;

import com.interface21.webmvc.servlet.mvc.tobe.handleradapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.handleradapter.HandlerExecutionAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.HandlerMappingRegistry;

public class DispatcherServletConfigurator {

    public static HandlerMappingRegistry handlerMappingRegistry(final AppConfig appConfig) {
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();

        final HandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(
                appConfig.getControllerBasePackage());
        handlerMappingRegistry.register(annotationHandlerMapping);

        return handlerMappingRegistry;
    }

    public static HandlerAdapterRegistry handlerAdapterRegistry() {
        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

        handlerAdapterRegistry.register(new HandlerExecutionAdapter());

        return handlerAdapterRegistry;
    }
}
