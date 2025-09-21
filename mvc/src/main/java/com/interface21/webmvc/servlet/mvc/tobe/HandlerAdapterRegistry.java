package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public HandlerAdapterRegistry() {
        registerHandlerAdapter();
    }

    public List<HandlerAdapter> getHandlerAdapters() {
        return handlerAdapters;
    }

    private void registerHandlerAdapter() {
        final AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();
        handlerAdapters.add(annotationHandlerAdapter);
    }
}
