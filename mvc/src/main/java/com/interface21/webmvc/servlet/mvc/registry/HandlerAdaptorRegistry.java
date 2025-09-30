package com.interface21.webmvc.servlet.mvc.registry;

import com.interface21.webmvc.servlet.mvc.adaptor.HandlerAdaptor;
import java.util.List;

public class HandlerAdaptorRegistry {

    private final List<HandlerAdaptor> handlerAdaptors;

    public HandlerAdaptorRegistry(final List<HandlerAdaptor> handlerAdaptors) {
        this.handlerAdaptors = handlerAdaptors;
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
