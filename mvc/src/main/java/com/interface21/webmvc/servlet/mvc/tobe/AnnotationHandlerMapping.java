package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final HandlerExecutions handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HandlerExecutions();
    }

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

    public HandlerExecution getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(uri, method));
    }
}
