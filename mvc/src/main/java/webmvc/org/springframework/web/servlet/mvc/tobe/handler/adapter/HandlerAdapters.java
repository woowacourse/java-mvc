package webmvc.org.springframework.web.servlet.mvc.tobe.handler.adapter;

import webmvc.org.springframework.web.servlet.mvc.tobe.handler.exception.HandlerAdapterNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter findHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.isSupported(handler))
                .findFirst()
                .orElseThrow(HandlerAdapterNotFoundException::new);
    }
}
