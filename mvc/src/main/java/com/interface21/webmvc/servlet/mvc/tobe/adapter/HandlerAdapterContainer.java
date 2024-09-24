package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.reflections.Reflections;

public class HandlerAdapterContainer {

    private final Set<HandlerAdapter> handlerAdapters;

    public HandlerAdapterContainer(final Object... basePackage) {
        Set<Class<? extends HandlerAdapter>> classes = findControllerClasses(basePackage);
        this.handlerAdapters = findHandlerAdapters(classes);
    }

    private Set<Class<? extends HandlerAdapter>> findControllerClasses(Object[] basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getSubTypesOf(HandlerAdapter.class);
    }

    private Set<HandlerAdapter> findHandlerAdapters(Set<Class<? extends HandlerAdapter>> adapters) {
        Set<HandlerAdapter> newHandlerAdapters = new HashSet<>();
        for (Class<? extends HandlerAdapter> aClass : adapters) {
            try {
                newHandlerAdapters.add(aClass.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException |
                     InvocationTargetException | NoSuchMethodException e) {
                throw new IllegalStateException("기본 생성자를 사용하여 " + aClass.getName() + " 인스턴스를 생성하는 중 문제가 발생했습니다.", e);
            }
        }
        return newHandlerAdapters;

    }

    public HandlerAdapter findHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("해당 핸들러를 지원하는 HandlerAdapter를 찾을 수 없습니다: " + handler));
    }
}
