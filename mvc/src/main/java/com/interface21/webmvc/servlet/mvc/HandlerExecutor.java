package com.interface21.webmvc.servlet.mvc;

import static org.reflections.Reflections.log;

import com.interface21.webmvc.servlet.ModelAndView;
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

    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) {
        Object handler = handlerMappingRegistry.getHandler(request);
        log.info("Handler 반환 타입: {}", handler.getClass().getSimpleName());

        HandlerAdapter adapter = handlerAdapterRegistry.getAdapter(handler);
        log.info("Adapter 선택됨: {}", adapter.getClass().getSimpleName());

        return adapter.handle(request, response, handler);
    }
}
