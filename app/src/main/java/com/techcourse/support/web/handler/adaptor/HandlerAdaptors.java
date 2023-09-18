package com.techcourse.support.web.handler.adaptor;

import webmvc.org.springframework.web.servlet.mvc.HandlerAdaptor;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerAdaptor;
import java.util.List;

public class HandlerAdaptors {

    private final List<HandlerAdaptor> values = List.of(new ManualHandlerAdaptor(), new AnnotationHandlerAdaptor());

    public HandlerAdaptor findHandlerAdaptor(final Object handler) {
        return values.stream()
                .filter(value -> value.supports(handler))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 Handler 입니다."));
    }
}
