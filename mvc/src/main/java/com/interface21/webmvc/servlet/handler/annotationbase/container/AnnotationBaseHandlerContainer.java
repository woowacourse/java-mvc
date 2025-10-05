package com.interface21.webmvc.servlet.handler.annotationbase.container;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.handler.HandlerContainer;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class AnnotationBaseHandlerContainer implements HandlerContainer {

    private static final Logger log = LoggerFactory.getLogger(AnnotationBaseHandlerContainer.class);

    private final HandlerExecutions handlerExecutions = HandlerExecutions.empty();

    public AnnotationBaseHandlerContainer() {
    }

    @Override
    public void initialize(Object... basePackages) {
        try {
            for (Object basePackage : basePackages) {
                handlerExecutions.addAll(getHandlerExecutions(basePackage));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private HandlerExecutions getHandlerExecutions(Object basePackage) throws Exception {
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

    @Override
    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.fromString(request.getMethod()));
        return handlerExecutions.get(handlerKey)
                .orElseThrow(() -> new IllegalArgumentException("No handler execution found for: " + handlerKey));
    }
}
