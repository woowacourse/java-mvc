
package com.techcourse;

import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public HandlerAdapters addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
        return this;
    }

    public Optional<HandlerAdapter> getHandlerAdapter(Object handler) {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.supports(handler)) {
                return Optional.of(handlerAdapter);
            }
        }
        return Optional.empty();
    }
}
