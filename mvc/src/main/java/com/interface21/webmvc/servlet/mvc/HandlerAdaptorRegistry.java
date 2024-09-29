package com.interface21.webmvc.servlet.mvc;

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
                    .orElseThrow(NullPointerException::new);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Can not find proper adaptor from handler: " + handler.toString());
        }
    }
}
