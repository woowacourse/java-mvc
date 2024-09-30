package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.HandlerExecutionAdaptor;
import com.interface21.webmvc.servlet.mvc.controller.ControllerAdaptor;
import java.util.ArrayList;
import java.util.List;

public class HandlerAdaptors {

    private final List<HandlerAdaptor> handlerAdaptors = new ArrayList<>();

    public HandlerAdaptors() {
        ControllerAdaptor controllerAdaptor = new ControllerAdaptor();
        handlerAdaptors.add(controllerAdaptor);

        HandlerExecutionAdaptor handlerExecutionAdaptor = new HandlerExecutionAdaptor();
        handlerAdaptors.add(handlerExecutionAdaptor);
    }

    public HandlerAdaptor findAdaptor(Object handler) {
        return handlerAdaptors.stream()
                .filter(handlerAdaptor -> handlerAdaptor.canExecute(handler))
                .findAny()
                .orElseThrow();
    }
}
