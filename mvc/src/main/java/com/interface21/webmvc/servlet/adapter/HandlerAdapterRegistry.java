package com.interface21.webmvc.servlet.adapter;

import com.interface21.webmvc.servlet.Handler;
import com.interface21.webmvc.servlet.HandlerType;
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
        handlerAdapters.put(HandlerType.CONTROLLER, new ControllerAdapter());
        handlerAdapters.put(HandlerType.HANDLER_EXECUTION, new HandlerExecutionAdapter());
        return new HandlerAdapterRegistry(handlerAdapters);
    }

    public ModelAndView handle(
            final Handler handler,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        return handlerAdapters
                .get(handler.type())
                .handle(handler, request, response);
    }
}
