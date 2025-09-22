package com.interface21.webmvc.servlet.mvc.tobe.dispatcherservlet;

import com.interface21.webmvc.servlet.mvc.tobe.handleradapter.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.handleradapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.handleradapter.HandlerExecutionAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.ManualHandlerMapping;

public class DispatcherServletConfigurator {

    public static HandlerMappingRegistry handlerMappingRegistry(final AppConfig appConfig) {
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();

        final HandlerMapping manualHandlerMapping = new ManualHandlerMapping(appConfig.getManualControllers());
        handlerMappingRegistry.register(manualHandlerMapping);
        final HandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(
                appConfig.getControllerBasePackage());
        handlerMappingRegistry.register(annotationHandlerMapping);

        return handlerMappingRegistry;
    }

    public static HandlerAdapterRegistry handlerAdapterRegistry() {
        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

        handlerAdapterRegistry.register(new ControllerHandlerAdapter());
        handlerAdapterRegistry.register(new HandlerExecutionAdapter());

        return handlerAdapterRegistry;
    }
}
