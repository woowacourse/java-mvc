package com.interface21.webmvc.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        handlerAdapters = new ArrayList<>();
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public ModelAndView execute(
            HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.support(handler))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("핸들러에 해당하는 핸들러 어댑터를 찾을 수 없습니다."))
                .handle(request, response, handler);
    }
}
