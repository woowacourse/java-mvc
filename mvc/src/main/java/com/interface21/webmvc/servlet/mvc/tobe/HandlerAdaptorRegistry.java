package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.servlet.HandlerAdaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class HandlerAdaptorRegistry {

    private final List<HandlerAdaptor> handlerAdaptors = new ArrayList<>();

    public void addHandlerAdaptors(HandlerAdaptor handlerAdaptor) {
        handlerAdaptors.add(handlerAdaptor);
    }

    public HandlerAdaptor getHandlerAdaptors(Object handler) {
        return handlerAdaptors.stream()
                .filter(handlerAdaptor -> handlerAdaptor.supports(handler))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("적합한 HandlerAdaptor를 찾지 못했습니다: " + handler.getClass()));
    }
}
