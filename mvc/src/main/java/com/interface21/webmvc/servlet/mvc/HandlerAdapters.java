package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void add(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public ModelAndView getMAV(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HandlerAdapter handlerAdapter1 = handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findAny()
                .orElseThrow();
        return handlerAdapter1.handle(request, response, handler);
    }
}
