package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
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

    public void initialize() {
        handlerExecutions.clear();

        final String[] packageNames = new String[basePackage.length];
        for (int index = 0; index < basePackage.length; index++) {
            packageNames[index] = basePackage[index].toString();
        }
        final Reflections reflections = new Reflections(packageNames);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllerClasses) {
            registerController(controllerClass);
        }

        log.info("Initialized AnnotationHandlerMapping with {} handlers!", handlerExecutions.size());
    }

    private void registerController(final Class<?> controllerClass) {
        try {
            final Object controller = controllerClass.getDeclaredConstructor().newInstance();
            for (Method method : controllerClass.getDeclaredMethods()) {
                final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                if (requestMapping == null) {
                    continue;
                }

                final RequestMethod[] requestMethods = requestMapping.method().length == 0
                        ? RequestMethod.values()
                        : requestMapping.method();
                for (RequestMethod requestMethod : requestMethods) {
                    final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
                    handlerExecutions.put(handlerKey, new HandlerExecution(controller, method));
                    log.info("Mapped {} {} to {}.{}", requestMethod, requestMapping.value(),
                            controllerClass.getSimpleName(), method.getName());
                }
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to initialize controller: " + controllerClass.getName(), e);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        return null;
    }
}
