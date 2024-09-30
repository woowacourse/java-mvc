package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.mvc.controller.InterfaceControllerAdaptor;
import com.interface21.webmvc.servlet.mvc.handler.AnnotationControllerAdaptor;
import java.util.ArrayList;
import java.util.List;

public class HandlerAdaptors {

    private final List<HandlerAdaptor> handlerAdaptors = new ArrayList<>();

    public HandlerAdaptors() {
        HandlerAdaptor interfaceControllerAdaptor = new InterfaceControllerAdaptor();
        handlerAdaptors.add(interfaceControllerAdaptor);

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
