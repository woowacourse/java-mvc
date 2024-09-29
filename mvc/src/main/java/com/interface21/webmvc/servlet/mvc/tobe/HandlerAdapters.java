package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.ServletException;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters() {
        this.handlerAdapters = new ArrayList<>();
    }

    public HandlerAdapter findHandlerAdapter(Object handler)
            throws Exception {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.isSupport(handler)) {
                return handlerAdapter;
            }
        }
        throw new ServletException("No handlerAdapter found");
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }
}
