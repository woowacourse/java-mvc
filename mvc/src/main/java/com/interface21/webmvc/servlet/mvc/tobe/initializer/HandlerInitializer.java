package com.interface21.webmvc.servlet.mvc.tobe.initializer;

import com.interface21.webmvc.servlet.mvc.tobe.InstanceMaker;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.pathfinder.RootPathFinder;
import com.interface21.webmvc.servlet.mvc.tobe.pathfinder.RootPathStrategy;
import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Set;

public class HandlerInitializer {

    private final RootPathFinder rootPathFinder;
    private final Reflections reflections;

    public HandlerInitializer(RootPathStrategy rootPathStrategy) {
        rootPathFinder = new RootPathFinder(rootPathStrategy);
        reflections = new Reflections(rootPathFinder.find());
    }

    public Set<HandlerMapping> getInitMappings() {
        Set<HandlerMapping> handlerMappings = new HashSet<>();

        Set<Class<? extends HandlerMapping>> handlerMappingClasses = reflections.getSubTypesOf(HandlerMapping.class);

        for (Class<?> handlerMappingClass : handlerMappingClasses) {
            HandlerMapping handlerMappingInstance = makeHandlerMapping(handlerMappingClass);

            handlerMappingInstance.initialize();
            handlerMappings.add(handlerMappingInstance);
        }
        return handlerMappings;
    }

    private HandlerMapping makeHandlerMapping(Class<?> handlerMappingClass) {
        if (handlerMappingClass == AnnotationHandlerMapping.class) {
            return (HandlerMapping) InstanceMaker.makeInstance(handlerMappingClass, rootPathFinder);
        }
        return (HandlerMapping) InstanceMaker.makeInstance(handlerMappingClass);
    }

    public Set<HandlerAdapter> getInitAdapters() {
        Set<HandlerAdapter> handlerAdapters = new HashSet<>();

        Set<Class<? extends HandlerAdapter>> handlerAdapterClasses = reflections.getSubTypesOf(HandlerAdapter.class);

        for (Class<?> handlerAdapterClass : handlerAdapterClasses) {
            HandlerAdapter handlerAdapter = (HandlerAdapter) InstanceMaker.makeInstance(handlerAdapterClass);
            handlerAdapters.add(handlerAdapter);
        }
        return handlerAdapters;
    }
}
