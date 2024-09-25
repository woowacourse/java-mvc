package com.techcourse;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;

import java.util.Map;

public class HandlerAdapterRegistry {

    private final Map<Class<?>, HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = Map.of(
                Controller.class, new AnnotationHandlerAdapter(),
                HandlerExecution.class, new AnnotationHandlerAdapter()
        );
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.get(handler.getClass());
    }
}
