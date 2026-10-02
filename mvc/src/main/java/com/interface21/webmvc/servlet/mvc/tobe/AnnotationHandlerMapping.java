package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize()
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        log.info("Initialized AnnotationHandlerMapping!");

        for (Object base : basePackage) {
            final Reflections reflections = new Reflections(base);
            final Set<Class<?>> controllerTypes = reflections.getTypesAnnotatedWith(Controller.class);
            for (Class<?> controllerType : controllerTypes) {
                final var controller = controllerType.getDeclaredConstructor().newInstance();
                Method[] methods = controllerType.getDeclaredMethods();
                for (Method method : methods) {
                    if (!method.isAnnotationPresent(RequestMapping.class)) {
                        continue;
                    }
                    RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
                    final var handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method()[0]);
                    final var handlerExecution = new HandlerExecution(controller, method);

                    handlerExecutions.put(handlerKey, handlerExecution);
                    log.info("Request {} {} -> Mapped to {}#{} on instance={}",
                            requestMapping.method()[0], requestMapping.value(),
                            controllerType.getSimpleName(), method.getName(),
                            handlerExecution
                    );
                }
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        // TODO: getRequestURL은 StringBuffer를 리턴하고, getRequestURI는 String을 리턴한다. 그 차이는?
        final var handlerExecution = handlerExecutions.get(new HandlerKey(
                request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod())
        ));
        log.info("Find handler for {} {}", request.getMethod(), request.getRequestURI());
        log.info("Found handler={}", handlerExecution);
        return handlerExecution;
    }
}
