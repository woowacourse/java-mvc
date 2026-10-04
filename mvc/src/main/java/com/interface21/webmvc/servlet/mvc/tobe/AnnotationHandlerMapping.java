package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
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
        final Set<Class<?>> controllers = findControllers();

        for (Class<?> controllerClass : controllers) {
            registerController(controllerClass);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        final String uri = request.getRequestURI();
        final RequestMethod method = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(uri, method);
        return handlerExecutions.get(handlerKey);
    }

    private void registerController(Class<?> controllerClass) {
        try {
            final Object controller = controllerClass.getDeclaredConstructor().newInstance();

            for (Method method : controllerClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    final String value = requestMapping.value();
                    RequestMethod[] requestMethods = requestMapping.method();
                    if (requestMethods.length == 0) {
                        requestMethods = RequestMethod.values();
                    }

                    for (RequestMethod requestMethod : requestMethods) {
                        handlerExecutions.put(
                                new HandlerKey(value, requestMethod),
                                new HandlerExecution(controller, method)
                        );
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private Set<Class<?>> findControllers() {
        final Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }
}
