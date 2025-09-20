package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Ref;
import java.util.*;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        handlerExecutions = new HashMap<>();
    }

    public void initialize() throws Exception {
        for (Object bp : basePackage) {
            Reflections reflections = new Reflections(bp, Scanners.TypesAnnotated);
            Set<Class<?>> controllerTypes = reflections.getTypesAnnotatedWith(Controller.class);

            for (Class<?> controllerType : controllerTypes) {
                Object controller = controllerType.getDeclaredConstructor().newInstance();

                List<Method> methods = Arrays.stream(controllerType.getDeclaredMethods())
                        .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                        .toList();

                methods.forEach(
                        method -> {
                            if (method.isAnnotationPresent(RequestMapping.class)) {
                                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                                handlerExecutions.put(
                                        new HandlerKey(requestMapping.value(), requestMapping.method()[0]),
                                        new HandlerExecution(controller, method)
                                );
                            }
                        }
                );
            }
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());

        HandlerKey handlerKey = new HandlerKey(requestURI, method);

        return handlerExecutions.get(handlerKey);
    }
}
