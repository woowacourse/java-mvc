package com.techcourse;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerDispatcher {
    
    private final HandlerAdapterRegistry handlerAdapterRegistry;
    
    public HandlerDispatcher(HandlerAdapterRegistry handlerAdapterRegistry) {
        this.handlerAdapterRegistry = handlerAdapterRegistry;
    }
    
    public ModelAndView dispatch(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerAdapter adapter = handlerAdapterRegistry.getHandlerAdapter(handler);
        return adapter.handle(request, response, handler);
    }
}