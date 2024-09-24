package com.interface21.webmvc.servlet.mvc.tobe.mapping;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;

import org.reflections.Reflections;

public class HandlerMappingContainer {

    private final Set<HandlerMapping> handlerMappings;

    public HandlerMappingContainer(final Object... basePackage) {
        Set<Class<? extends HandlerMapping>> classes = findControllerClasses(basePackage);
        this.handlerMappings = findHandlerMappings(classes);
    }

    private Set<Class<? extends HandlerMapping>> findControllerClasses(Object[] basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getSubTypesOf(HandlerMapping.class);
    }

    private Set<HandlerMapping> findHandlerMappings(Set<Class<? extends HandlerMapping>> adapters) {
        Set<HandlerMapping> newHandlerAdapters = new HashSet<>();
        for (Class<? extends HandlerMapping> aClass : adapters) {
            try {
                newHandlerAdapters.add(aClass.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException |
                     InvocationTargetException | NoSuchMethodException e) {
                throw new IllegalStateException("기본 생성자를 사용하여 " + aClass.getName() + " 인스턴스를 생성하는 중 문제가 발생했습니다.", e);
            }
        }
        return newHandlerAdapters;
    }


    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
