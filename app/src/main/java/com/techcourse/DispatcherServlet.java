package com.techcourse;

import com.interface21.webmvc.servlet.AbstractDispatcherServlet;
import com.interface21.webmvc.servlet.adapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mapping.HandlerMappingRegistry;

public class DispatcherServlet extends AbstractDispatcherServlet {

    private DispatcherServlet(
            final HandlerMappingRegistry handlerMappingRegistry,
            final HandlerAdapterRegistry handlerAdapterRegistry
    ) {
        super(handlerMappingRegistry, handlerAdapterRegistry);
    }

    public static DispatcherServlet initialize() {
        final HandlerMappingRegistry handlerMappingRegistry = HandlerMappingRegistry.initialize(
                "com.techcourse.controller",
                ManualMappingConfig.createManualMapping()
        );
        final HandlerAdapterRegistry handlerAdapterRegistry = HandlerAdapterRegistry.initialize();
        return new DispatcherServlet(handlerMappingRegistry, handlerAdapterRegistry);
    }
}
