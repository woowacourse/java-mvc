package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.mvc.handler.AnnotationControllerAdaptor;
import java.util.ArrayList;
import java.util.List;

public class HandlerAdaptors {

    private final List<HandlerAdaptor> handlerAdaptors = new ArrayList<>();

    public HandlerAdaptors() {
        HandlerAdaptor annotationControllerAdaptor = new AnnotationControllerAdaptor();
        handlerAdaptors.add(annotationControllerAdaptor);
    }

    public HandlerAdaptor findAdaptor(Object handler) {
        return handlerAdaptors.stream()
                .filter(handlerAdaptor -> handlerAdaptor.canExecute(handler))
                .findAny()
                .orElseThrow();
    }
}
