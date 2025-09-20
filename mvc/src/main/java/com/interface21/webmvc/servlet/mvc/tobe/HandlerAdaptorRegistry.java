package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdaptorRegistry {

    private final List<HandlerAdaptor> handlerAdaptors = new ArrayList<>();

    public void addHandlerAdaptor(final HandlerAdaptor handlerAdaptor) {
        handlerAdaptors.add(handlerAdaptor);
    }

    public HandlerAdaptor getHandlerAdaptor(final Object handler) {
        for (final HandlerAdaptor handlerAdaptor : handlerAdaptors) {
            if (handlerAdaptor.support(handler)) {
                return handlerAdaptor;
            }
        }
        throw new IllegalArgumentException("handlerAdaptor가 존재하지 않는 않는 요청입니다.");
    }
}
