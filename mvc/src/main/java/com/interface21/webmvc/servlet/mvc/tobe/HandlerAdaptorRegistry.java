package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdaptorRegistry {

    List<HandlerAdaptor> handlerAdaptors = new ArrayList<>();

    public void addHandlerAdaptor(HandlerAdaptor handlerAdaptor) {
        handlerAdaptors.add(handlerAdaptor);
    }

    public HandlerAdaptor getHandlerAdaptor(Object handler) {
        for (HandlerAdaptor handlerAdaptor : handlerAdaptors) {
            if (handlerAdaptor.support(handler)) {
                return handlerAdaptor;
            }
        }
        throw new IllegalArgumentException("handlerAdaptor가 존재하지 않는 않는 요청입니다.");
    }
}
