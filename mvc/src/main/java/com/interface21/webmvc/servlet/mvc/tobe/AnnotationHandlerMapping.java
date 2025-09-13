package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
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
        final var reflections = new Reflections(basePackage);
        final var controllers = reflections.get(Scanners.TypesAnnotated.of(Controller.class).asClass());
        for (Class<?> controller : controllers) {
            final Object controllerInstance;
            try {
                controllerInstance = controller.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            for (Method method : controller.getDeclaredMethods()) {
                if (!method.isAnnotationPresent(RequestMapping.class)) continue;

                final var annotation = method.getAnnotation(RequestMapping.class);
                final var url = annotation.value();
                final var requestMethods = annotation.method();
                for (RequestMethod requestMethod : requestMethods) {
                    handlerExecutions.put(new HandlerKey(url, requestMethod), new HandlerExecution(controllerInstance, method));
                }
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        final var uri = request.getRequestURI();
        final var method = request.getMethod();
        final var handlerKey = new HandlerKey(uri, RequestMethod.valueOf(method.toUpperCase()));
        if (!handlerExecutions.containsKey(handlerKey)) throw new IllegalArgumentException("No Handler For " + handlerKey);
        return handlerExecutions.get(handlerKey);
    }
}
