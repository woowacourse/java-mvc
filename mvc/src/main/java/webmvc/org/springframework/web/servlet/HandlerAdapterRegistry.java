package webmvc.org.springframework.web.servlet;

import webmvc.org.springframework.web.servlet.exception.UncheckedServletException;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;

import java.util.ArrayList;
import java.util.List;

public final class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new ArrayList<>();
    }

    void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    HandlerAdapter getHandlerAdapter(final Object handler) {
        for (final HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.isSupport(handler)) {
                return handlerAdapter;
            }
        }

        throw new UncheckedServletException(new IllegalArgumentException("지원하는 Handler가 존재하지 않습니다."));
    }
}
