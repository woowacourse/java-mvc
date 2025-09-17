package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutor {

    private final HandlerAdapterRegistry adapterRegistry;

    public HandlerExecutor(HandlerAdapterRegistry adapterRegistry) {
        this.adapterRegistry = adapterRegistry;
    }

    public ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerAdapter adapter = adapterRegistry.getHandlerAdapter(handler);
        return adapter.handle(handler, request, response);
    }
}
