package com.interface21.webmvc.servlet.adapter;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class HandlerAdapterRegistry {

    private final Map<HandlerType, HandlerAdapter> handlerAdapters;

    public static HandlerAdapterRegistry initialize() {
        final Map<HandlerType, HandlerAdapter> handlerAdapters = new HashMap<>();
        // todo reflection
        handlerAdapters.put(HandlerType.CONTROLLER, new ControllerHandlerAdapter());
        handlerAdapters.put(HandlerType.HANDLER_EXECUTION, new HandlerExecutionHandlerAdapter());
        return new HandlerAdapterRegistry(handlerAdapters);
    }

    public ModelAndView handle(
            final Object handler,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        return switch (handler) {
            case final Controller controller ->
                    handlerAdapters.get(HandlerType.CONTROLLER).handle(controller, request, response);
            case final HandlerExecution handlerExecution ->
                    handlerAdapters.get(HandlerType.HANDLER_EXECUTION).handle(handlerExecution, request, response);
            default -> throw new IllegalArgumentException("Unhandled handler type: " + handler.getClass());
        };
    }

    private enum HandlerType {
        CONTROLLER, HANDLER_EXECUTION
    }
}
