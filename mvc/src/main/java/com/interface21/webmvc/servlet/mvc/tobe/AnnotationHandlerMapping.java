package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        try {
            for (Object basePackage : basePackages) {
                handlerExecutions.putAll(getHandlerExecutions(basePackage));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Map<HandlerKey, HandlerExecution> getHandlerExecutions(Object basePackage) throws Exception {
        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
        Set<Class<?>> controllerClasses = new Reflections(basePackage).getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllerClasses) {
            Object instance = controllerClass.getDeclaredConstructor().newInstance();

            List<Method> methods = List.of(controllerClass.getDeclaredMethods());
            String basePath = controllerClass.getAnnotation(Controller.class).path();

            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

                    String path = basePath + requestMapping.value();
                    List<RequestMethod> requestMethods = Arrays.asList(requestMapping.method());

                    requestMethods.stream()
                            .map(requestMethod -> new HandlerKey(path, requestMethod))
                            .forEach(handlerKey ->
                                    handlerExecutions.put(handlerKey, new HandlerExecution(instance, method))
                            );
                }
            }
        }

        return handlerExecutions;
    }

    public Object getHandler(final HttpServletRequest request) {

        return handlerExecutions.get(
                new HandlerKey(request.getRequestURI(), RequestMethod.fromString(request.getMethod()))
        );
    }
}
