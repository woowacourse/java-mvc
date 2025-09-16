package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
        final var controllers = new Reflections(basePackage).getTypesAnnotatedWith(Controller.class);
        controllers.forEach(controller -> Arrays.stream(controller.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(requestMappingMethod -> {
                    var handlerExecution = createHandlerExecution(controller, requestMappingMethod);
                    var requestMappingAnnotation = requestMappingMethod.getAnnotation(RequestMapping.class);
                    extractHandlerExecutions(handlerExecution, requestMappingAnnotation);
                })
        );
    }

    public Object getHandler(final HttpServletRequest request) {
        final var requestURI = request.getRequestURI();
        final var method = request.getMethod();
        final var handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }

    private HandlerExecution createHandlerExecution(final Class<?> controller, final Method method) {
        try {
            final Constructor<?> constructor = controller.getDeclaredConstructor();
            return new HandlerExecution(constructor.newInstance(), method);
        } catch (final Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void extractHandlerExecutions(
            final HandlerExecution handlerExecution,
            final RequestMapping requestMappingMethod
    ) {
        try {
            final var value = requestMappingMethod.value();
            final var httpMethods = requestMappingMethod.method();
            if (httpMethods.length == 0) {
                for (RequestMethod requestMethod : RequestMethod.values()) {
                    final var handlerKey = new HandlerKey(value, requestMethod);
                    handlerExecutions.put(handlerKey, handlerExecution);
                }
                return;
            }
            for (RequestMethod requestMethod : httpMethods) {
                final var handlerKey = new HandlerKey(value, requestMethod);
                handlerExecutions.put(handlerKey, handlerExecution);
            }
        } catch (final Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
