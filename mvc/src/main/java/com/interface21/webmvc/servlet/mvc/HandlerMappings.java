package com.interface21.webmvc.servlet.mvc;

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
        reflections.getSubTypesOf(HandlerMapping.class)
                .forEach(handlerMapping -> {
                    try {
                        handlerMappings.add(handlerMapping.getDeclaredConstructor().newInstance());
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public Object getHandler(HttpServletRequest request) {
        HandlerMapping handlerMapping1 = handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.supports(request))
                .findAny()
                .orElseThrow();
        return handlerMapping1.getHandler(request);
    }
}
