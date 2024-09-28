package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import com.interface21.webmvc.servlet.mvc.tobe.exception.HandlerAdapterCreationException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.reflections.Reflections;

public class HandlerAdapterRegistry {

    private static final String BASE_PACKAGE = "com.interface21.webmvc.servlet.mvc.tobe.adapter";

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public HandlerAdapterRegistry() {
        initHandlerAdapter();
    }

    private void initHandlerAdapter() {
        Reflections reflections = new Reflections(BASE_PACKAGE);
        reflections.getSubTypesOf(HandlerAdapter.class)
                .stream()
                .map(this::createHandlerAdapterInstance)
                .forEach(handlerAdapters::add);
    }

    private HandlerAdapter createHandlerAdapterInstance(Class<? extends HandlerAdapter> handlerAdapterClass) {
        try {
            return handlerAdapterClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new HandlerAdapterCreationException(e.getMessage());
        }
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElse(null);
    }
}
