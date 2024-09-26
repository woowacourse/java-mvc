package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<HandlerAdapter> handlerAdapter = handlerAdapters.stream()
                .filter(adapter -> adapter.support(handler))
                .findAny();

        if (handlerAdapter.isEmpty()) {
            return null;
        }
        return handlerAdapter.get().handle(request, response, handler);
    }
}
