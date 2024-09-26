package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.mvc.tobe.exception.HandlerMappingCreationException;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import org.reflections.Reflections;

public class HandlerMappingRegistry {

    private static final HandlerMappingRegistry INSTANCE = new HandlerMappingRegistry();

    private List<HandlerMapping> handlerMappings;

    private HandlerMappingRegistry() {
    }

    public static HandlerMappingRegistry getInstance() {
        return INSTANCE;
    }

    public void initialize(Object... basePackage) {
        if (handlerMappings == null) {
            handlerMappings = findHandlerMapping(basePackage);
            handlerMappings.forEach(HandlerMapping::initialize);
        }
    }

    private List<HandlerMapping> findHandlerMapping(Object... basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getSubTypesOf(HandlerMapping.class)
                .stream()
                .map(this::createHandlerMappingInstance)
                .toList();
    }

    private HandlerMapping createHandlerMappingInstance(Class<? extends HandlerMapping> handlerMappingClass) {
        try {
            return handlerMappingClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new HandlerMappingCreationException(e.getMessage());
        }
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
}
