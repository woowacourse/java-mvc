package com.interface21.webmvc.servlet.mvc.handler.adapter;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class HandlerAdapters {
    private final List<HandlerAdapter> adapters;

    public HandlerAdapters(List<HandlerAdapter> adapters) {
        this.adapters = new ArrayList<>(adapters);
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HandlerAdapter handlerAdapter = adapters.stream()
                .filter(adapter -> adapter.canHandle(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format(
                        "%s %s를 처리할 수 있는 어댑터가 존재하지 않습니다.", request.getMethod(), request.getRequestURI())));
        return handlerAdapter.handle(request, response, handler);
    }

}
