package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdaptorRegistry {

    private final List<HandlerAdaptor> handlerAdaptors;

    public HandlerAdaptorRegistry() {
        this.handlerAdaptors = new ArrayList<>();
    }

    public void add(HandlerAdaptor handlerAdaptor) {
        handlerAdaptors.add(handlerAdaptor);
    }

    public HandlerAdaptor findHandlerAdaptor(Object handler) {
        return handlerAdaptors.stream()
                .filter(handlerAdaptor -> handlerAdaptor.isSupported(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("요청에 알맞는 Handler Adaptor를 찾을 수 없습니다."));
    }
}
