package com.interface21.webmvc.servlet.mvc.handler.mapping;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;

public class HandlerMappingRegistry {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();
    private final Object[] basePackages;

    public HandlerMappingRegistry(final Object... basePackages) {
        this.basePackages = basePackages;
    }

    public void initialize2() throws Exception {
//        final HandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping();
//        final HandlerMapping manualHandlerAdapter = new ManualHandlerMapping();

//        final Reflections reflections = new Reflections(basePackages);
//        final Set<Class<? extends HandlerMapping>> classes = reflections.getSubTypesOf(HandlerMapping.class);
//        for (Class<? extends HandlerMapping> clazz : classes) {
//            final HandlerMapping handlerMapping = createInstance(clazz);
//            addHandlerMapping(handlerMapping);
//        }
    }

    public void initialize() throws Exception {
        final Reflections reflections = new Reflections(basePackages);
        final Set<Class<? extends HandlerMapping>> classes = reflections.getSubTypesOf(HandlerMapping.class);
        for (Class<? extends HandlerMapping> clazz : classes) {
            final HandlerMapping handlerMapping = createInstance(clazz);
            addHandlerMapping(handlerMapping);
        }
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(final HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            final Object handler = handlerMapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        throw new RuntimeException("No handler found for request URI : " + request.getRequestURI());
    }

    private HandlerMapping createInstance(final Class<? extends HandlerMapping> clazz) throws Exception {
        final Constructor<? extends HandlerMapping> constructor = clazz.getDeclaredConstructor(Object[].class);
        final HandlerMapping handlerMapping = constructor.newInstance(basePackages);
        handlerMapping.initialize();

        return handlerMapping;
    }
}
