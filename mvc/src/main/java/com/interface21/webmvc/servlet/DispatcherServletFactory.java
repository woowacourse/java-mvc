package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.handler.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.handler.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.handler.adapter.HandlerAdapters;
import com.interface21.webmvc.servlet.mvc.handler.mapping.HandlerMappings;
import java.util.List;

public class DispatcherServletFactory {

    private DispatcherServletFactory() {
    }

    public static DispatcherServlet createWithBasePackages(final Object... basePackages) {
        HandlerMappings handlerMappings = new HandlerMappings(List.of(new AnnotationHandlerMapping(basePackages)));
        HandlerAdapters handlerAdapters = new HandlerAdapters(List.of(new AnnotationHandlerAdapter()));

        return new DispatcherServlet(handlerMappings, handlerAdapters);
    }
}
