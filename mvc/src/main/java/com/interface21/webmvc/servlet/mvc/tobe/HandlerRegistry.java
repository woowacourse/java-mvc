package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerRegistry {

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public HandlerRegistry(HandlerMappingRegistry handlerMappingRegistry,
                           HandlerAdapterRegistry handlerAdapterRegistry) {
        this.handlerMappingRegistry = handlerMappingRegistry;
        this.handlerAdapterRegistry = handlerAdapterRegistry;
    }

    public Object handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object controller = handlerMappingRegistry.getHandler(request);
        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(controller);
        return handlerAdapter.handle(request, response, controller);
    }

    @Override
    public String toString() {
        return "HandlerRegistry{" +
                "handlerMappingRegistry=" + handlerMappingRegistry +
                ", handlerAdapterRegistry=" + handlerAdapterRegistry +
                '}';
    }
}
