package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.exception.UncheckedReflectiveOperationException;
import org.reflections.Reflections;

public class RequestHandlerMappings {

    private final String[] basePackage;
    private final Set<RequestHandlerMapping> mappings;

    public RequestHandlerMappings(final String... basePackage) {
        this.basePackage = basePackage;
        this.mappings = new HashSet<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<RequestHandlerMapping> requestHandlerMappings = reflections.getSubTypesOf(RequestHandlerMapping.class)
                .stream()
                .map(this::getInstance)
                .collect(Collectors.toUnmodifiableSet());
        mappings.addAll(requestHandlerMappings);
    }

    private RequestHandlerMapping getInstance(Class<?> clazz) {
        try {
            return (RequestHandlerMapping) clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException |
                 InvocationTargetException |
                 IllegalAccessException |
                 NoSuchMethodException e) {
            throw new UncheckedReflectiveOperationException(e);
        }
    }
}
