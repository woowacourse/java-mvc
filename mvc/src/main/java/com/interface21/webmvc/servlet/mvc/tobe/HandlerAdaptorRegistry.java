package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdaptorRegistry {

    private final List<HandlerAdaptor> handlerAdaptors = new ArrayList<>();

    public void addHandlerAdaptor(HandlerAdaptor handlerAdaptor) {
        handlerAdaptors.add(handlerAdaptor);
    }

    public HandlerAdaptor getHandlerAdaptor(Object handler) {
        try {
            return handlerAdaptors.stream()
                    .filter(handlerAdaptor -> handlerAdaptor.supports(handler))
                    .findAny()
                    .orElseThrow(() -> new NullPointerException("Can not find proper adaptor from handler: " + handler.toString()));
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
