package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);

        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> clazz : classes) {
            try {
                final Map<HandlerKey, HandlerExecution> foundExecutions = Arrays.stream(clazz.getDeclaredMethods())
                        .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                        .flatMap(method -> {
                            final RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                            final HandlerExecution execution = createHandlerExecution(clazz, method);

                            return Arrays.stream(mapping.method())
                                    .map(requestMethod -> {
                                        HandlerKey key = new HandlerKey(mapping.value(), requestMethod);
                                        return new SimpleEntry<>(key, execution);
                                    });
                        })
                        .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
                
                this.handlerExecutions.putAll(foundExecutions);
            } catch (final Exception e) {
                log.error("Failed to initialize controller: {}", clazz.getName(), e);
            }
        }

        log.info("Initialized AnnotationHandlerMapping!");
        handlerExecutions.forEach((handlerKey, handlerExecution)
                -> log.info("Path : {}, handlerExecution : {}", handlerKey, handlerExecution));
    }

    private HandlerExecution createHandlerExecution(Class<?> clazz, Method method) {
        final Object instance = getInstance(clazz);
        return new HandlerExecution(instance, method);
    }

    private Object getInstance(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));
        log.debug("[AnnotationHandlerMapping] Request Mapping Uri : {}", requestURI);
        return handlerExecutions.get(handlerKey);
    }
}
