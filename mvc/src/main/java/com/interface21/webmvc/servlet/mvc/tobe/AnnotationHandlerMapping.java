package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        try {
            final Reflections reflections = new Reflections(basePackage);

            final Set<Class<?>> controllerClazzs = reflections.getTypesAnnotatedWith(Controller.class);
            for (final Class<?> controllerClazz : controllerClazzs) {
                mapController(controllerClazz);
            }
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String value = request.getRequestURI();
        final RequestMethod method = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(value, method);
        return handlerExecutions.get(handlerKey);
    }

    private void mapController(final Class<?> controllerClazz) throws Exception {
        final Method[] controllerMethods = controllerClazz.getDeclaredMethods();
        final Object controllerInstance = controllerClazz.getDeclaredConstructor().newInstance();
        for (final Method controllerMethod : controllerMethods) {
            mapControllerMethod(controllerMethod, controllerInstance);
        }
    }

    private void mapControllerMethod(final Method controllerMethod, final Object controllerInstance) {
        if (!controllerMethod.isAnnotationPresent(RequestMapping.class)) {
            return;
        }

        final RequestMapping annotation = controllerMethod.getAnnotation(RequestMapping.class);
        final String value = annotation.value();
        final RequestMethod[] methods = annotation.method();

        for (final RequestMethod method : methods) {
            final HandlerKey handlerKey = new HandlerKey(value, method);
            final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, controllerMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }
}
