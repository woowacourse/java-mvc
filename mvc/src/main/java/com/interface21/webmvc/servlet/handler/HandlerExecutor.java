package com.interface21.webmvc.servlet.handler;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.exception.NotFoundHandlerAdapterException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

public class HandlerExecutor {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerExecutor(List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HandlerAdapter handlerAdapter =  handlerAdapters.stream()
                .filter(adapter -> adapter.canHandle(handler))
                .findFirst()
                .orElseThrow(NotFoundHandlerAdapterException::new);

        return handlerAdapter.handle(request, response, handler);
    }
}
