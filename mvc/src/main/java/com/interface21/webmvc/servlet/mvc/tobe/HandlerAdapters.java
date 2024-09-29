package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.ServletException;

import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters(List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
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
}
