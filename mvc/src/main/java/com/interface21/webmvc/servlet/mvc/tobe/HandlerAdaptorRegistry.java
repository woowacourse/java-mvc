package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdaptorRegistry {

    private final List<HandlerAdaptor> handlerAdaptors = new ArrayList<>();
    

    public void addHandlerAdaptor(HandlerAdaptor handlerAdaptor) {
        handlerAdaptors.add(handlerAdaptor);
    }

    public HandlerAdaptor getHandlerAdaptor(Object handler) {
        for (HandlerAdaptor handlerAdaptor : handlerAdaptors) {
            if (handlerAdaptor.supports(handler)) {
                return handlerAdaptor;
            }
        }

        throw new IllegalStateException("처리할 수 있는 어댑터가 없습니다.");
    }
}
