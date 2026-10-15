package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutor {

    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public HandlerExecutor(final HandlerAdapterRegistry handlerAdapterRegistry) {
        this.handlerAdapterRegistry = handlerAdapterRegistry;
    }

    // 핸들러의 종류와 상관없이 알맞은 HandlerAdapter를 골라 실행하고 ModelAndView를 돌려준다
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                               final Object handler) throws Exception {
        final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
        return handlerAdapter.handle(request, response, handler);
    }
}
