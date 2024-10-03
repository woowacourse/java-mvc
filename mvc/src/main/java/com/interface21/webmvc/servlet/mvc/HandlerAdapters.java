package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> adapters = new ArrayList<>();

    public void add(HandlerAdapter handlerAdapter) {
        adapters.add(handlerAdapter);
    }

    public ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HandlerAdapter handlerAdapter = adapters.stream()
                .filter(adapter -> adapter.canHandle(handler))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("처리할 어댑터가 존재하지 않습니다."));

        return handlerAdapter.handle(handler, request, response);
    }
}
