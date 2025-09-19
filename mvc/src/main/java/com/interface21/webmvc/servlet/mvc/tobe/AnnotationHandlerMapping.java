package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final HandlerExecutions handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = HandlerExecutions.empty();
    }

    public void initialize() {
        try {
            for (Object basePackage : basePackages) {
                handlerExecutions.addAll(getHandlerExecutions(basePackage));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public HandlerExecutions getHandlerExecutions(Object basePackage) throws Exception {
        HandlerExecutions handlerExecutions = HandlerExecutions.empty();
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

                    requestMethods.forEach(requestMethod ->
                            handlerExecutions.add(
                                    new HandlerKey(path, requestMethod),
                                    new HandlerExecution(instance, method)
                            )
                    );
                }
            }
        }

        return handlerExecutions;
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.fromString(request.getMethod()));
        return handlerExecutions.get(handlerKey)
                .orElseThrow(() -> new IllegalArgumentException("No handler execution found for: " + handlerKey));
    }
}
