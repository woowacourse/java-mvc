package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutor {

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public HandlerExecutor(HandlerMappingRegistry handlerMappingRegistry, HandlerAdapterRegistry handlerAdapterRegistry) {
        this.handlerMappingRegistry = handlerMappingRegistry;
        this.handlerAdapterRegistry = handlerAdapterRegistry;
    }

    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object handler = handlerMappingRegistry.getHandler(request)
                .orElseThrow(() -> new IllegalArgumentException(
                        "요청 핸들러를 찾을 수 없습니다: " + request.getRequestURI()
                ));
        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
        return handlerAdapter.handle(request, response, handler);
    }
}
