package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutor {

    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public HandlerExecutor(HandlerAdapterRegistry handlerAdapterRegistry) {
        this.handlerAdapterRegistry = handlerAdapterRegistry;
    }

    public ModelAndView execute(Object handler, HttpServletRequest request, HttpServletResponse response) {
        try {
            HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            return handlerAdapter.handle(request, response, handler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
