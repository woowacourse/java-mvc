package com.techcourse;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void initialize() {
        ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();

        handlerAdapters.add(controllerHandlerAdapter);
        handlerAdapters.add(annotationHandlerAdapter);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("가능한 HandlerAdapter가 존재하지 않습니다."));
    }

}
