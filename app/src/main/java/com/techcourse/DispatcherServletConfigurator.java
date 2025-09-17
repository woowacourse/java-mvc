package com.techcourse;

import com.interface21.webmvc.servlet.mvc.tobe.handleradapter.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.handleradapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.handleradapter.HandlerExecutionAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.HandlerMappingRegistry;

public class DispatcherServletConfigurator {

    public static HandlerMappingRegistry handlerMappingRegistry() {
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();

        final HandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        handlerMappingRegistry.register(manualHandlerMapping);
        final HandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
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
