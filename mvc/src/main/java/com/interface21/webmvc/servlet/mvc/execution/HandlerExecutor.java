package com.interface21.webmvc.servlet.mvc.execution;

import com.interface21.webmvc.servlet.mvc.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.adapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.exception.NoHandlerFoundException;
import com.interface21.webmvc.servlet.mvc.mapping.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.view.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutor {
    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public HandlerExecutor(HandlerMappingRegistry handlerMappingRegistry,
                           HandlerAdapterRegistry handlerAdapterRegistry) {
        this.handlerMappingRegistry = handlerMappingRegistry;
        this.handlerAdapterRegistry = handlerAdapterRegistry;
    }

    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response)
            throws NoHandlerFoundException {
        Object handler = handlerMappingRegistry.getHandler(request);
        HandlerAdapter adapter = handlerAdapterRegistry.getAdapter(handler);
        return adapter.handle(request, response, handler);
    }
}
