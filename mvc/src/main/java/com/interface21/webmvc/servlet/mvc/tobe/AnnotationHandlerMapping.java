package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Arrays.stream(basePackage)
                        .forEach(basePackage -> {
                            Reflections reflections = new Reflections(basePackage);
                            Set<Class<?>> handlers = reflections.getTypesAnnotatedWith(Controller.class);
                            handlers.stream()
                                    .forEach(handler -> {
                                        Method[] methods = handler.getMethods();
                                        Arrays.stream(methods)
                                                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                                                .forEach(method -> {
                                                    RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
                                                    String url = requestMapping.value();
                                                    RequestMethod[] requestMethods = requestMapping.method();
                                                    Arrays.stream(requestMethods)
                                                            .forEach(requestMethod ->  {
                                                                HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                                                                HandlerExecution handlerExecution = null;
                                                                try {
                                                                    handlerExecution = new HandlerExecution(handler.newInstance(), method);
                                                                } catch (InstantiationException e) {
                                                                    throw new RuntimeException(e);
                                                                } catch (IllegalAccessException e) {
                                                                    throw new RuntimeException(e);
                                                                }
                                                                handlerExecutions.put(handlerKey, handlerExecution);
                                                            });

                                                });
                                    });
                        });
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        String method = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(url, RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }
}
