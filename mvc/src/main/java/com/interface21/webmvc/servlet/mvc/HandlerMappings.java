package com.interface21.webmvc.servlet.mvc;

import com.interface21.NotFoundException;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings() {
        this.handlerMappings = new ArrayList<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections(ClasspathHelper.forJavaClassPath());
        List<HandlerMapping> objects = reflections.getSubTypesOf(HandlerMapping.class).stream()
                .map(this::createHandlerMapping)
                .toList();
        objects.forEach(HandlerMapping::initialize);
        handlerMappings.addAll(objects);
    }

    private HandlerMapping createHandlerMapping(Class<?> clazz) {
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            return (HandlerMapping) instance;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            try {
                return handlerMapping.getHandler(request);
            } catch (NotFoundException e) {}
        }
        throw new NotFoundException("일치하는 handler가 없습니다");
    }
}
