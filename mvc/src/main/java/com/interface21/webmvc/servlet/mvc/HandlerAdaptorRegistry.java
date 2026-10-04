package com.interface21.webmvc.servlet.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class HandlerAdaptorRegistry {

    private final List<HandlerAdaptor> handlerAdaptors;

    public HandlerAdaptorRegistry() {
        this.handlerAdaptors = new ArrayList<>();
    }

    public void addHandlerAdaptor(final HandlerAdaptor handlerAdaptor) {
        this.handlerAdaptors.add(handlerAdaptor);
    }

    public HandlerAdaptor getHandlerAdaptor(final Object handler) {
        for (final HandlerAdaptor adaptor : handlerAdaptors) {
            if (adaptor.supports(handler)) {
                return adaptor;
            }
        }
        throw new NoSuchElementException("핸들러에 대응하는 어댑터가 없습니다.");
    }
}
