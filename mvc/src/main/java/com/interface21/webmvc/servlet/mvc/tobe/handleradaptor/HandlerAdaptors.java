package com.interface21.webmvc.servlet.mvc.tobe.handleradaptor;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdaptor;
import java.util.Arrays;
import java.util.List;

public class HandlerAdaptors {

    private final List<HandlerAdaptor> handlerAdaptors;

    public HandlerAdaptors(HandlerAdaptor... handlerAdaptors) {
        this.handlerAdaptors = Arrays.stream(handlerAdaptors).toList();
    }

    public HandlerAdaptor findHandlerAdaptor(Object handler) {
        return handlerAdaptors.stream()
                .filter(handlerAdaptor -> handlerAdaptor.support(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하는 handlerAdaptor 가 없습니다."));
    }
}
