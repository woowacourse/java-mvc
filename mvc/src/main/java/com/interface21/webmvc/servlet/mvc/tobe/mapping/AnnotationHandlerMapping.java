package com.interface21.webmvc.servlet.mvc.tobe.mapping;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutions;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final HandlerExecutions handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HandlerExecutions();
    }

    @Override
    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);

        Map<Class<?>, Object> instances = classes.stream()
                .collect(Collectors.toMap(clazz -> clazz, this::createInstance, (o1, o2) -> o1));

        for (Class<?> clazz : classes) {
            Arrays.stream(clazz.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .forEach(method -> {
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        HandlerExecution handlerExecution = new HandlerExecution(instances.get(clazz), method);
                        handlerExecutions.put(requestMapping, handlerExecution);
                    });
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Object createInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException exception) {
            throw new RuntimeException(exception);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(uri, method));
    }
}
