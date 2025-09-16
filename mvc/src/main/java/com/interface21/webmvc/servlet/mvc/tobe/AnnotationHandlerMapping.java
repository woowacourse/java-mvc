package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
    
    public AnnotationHandlerMapping(final String basePackage, final String... others) {
        this.basePackage = Stream.concat(Stream.of(basePackage), Arrays.stream(others)).toArray(String[]::new);
    }

    @Override
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
                    final var handlerKey = new HandlerKey(url, requestMethod);
                    if (handlerExecutions.containsKey(handlerKey)) throw new IllegalStateException("HandlerKey Conflict For " + handlerKey);
                    handlerExecutions.put(handlerKey, new HandlerExecution(controllerInstance, method));
                }
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
        handlerExecutions.keySet()
                .forEach(handlerKey -> log.info("Path : {}, Controller : {}", handlerKey.url(), handlerExecutions.get(handlerKey).getHandler().getClass()));
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final var uri = request.getRequestURI();
        final var method = request.getMethod();
        final var handlerKey = new HandlerKey(uri, RequestMethod.valueOf(method.toUpperCase()));
        return handlerExecutions.get(handlerKey);
    }
}
