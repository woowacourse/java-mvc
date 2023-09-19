package com.techcourse.support.web.adaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdaptor;

public class HandlerAdaptors {

    private final List<HandlerAdaptor> adaptors = new ArrayList<>();

    public HandlerAdaptors() {
        adaptors.add(new AnnotationHandlerAdaptor());
        adaptors.add(new ManualHandlerAdaptor());
    }

    public HandlerAdaptor getAdaptor(Object handler) {
        return adaptors.stream()
                .filter(adaptor -> adaptor.supports(handler))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("처리할 수 있는 어댑터가 존재하지 않습니다."));
    }

}
