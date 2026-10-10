package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        final var controllers = new ControllerScanner(basePackage).getControllers();
        controllers.forEach(this::registerHandlers);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final var handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }

    private void registerHandlers(final Class<?> controllerClass, final Object controller) {
        Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> registerHandler(controller, method));
    }

    private void registerHandler(final Object controller, final Method method) {
        final var mapping = method.getAnnotation(RequestMapping.class);
        var httpMethods = mapping.method();
        if (httpMethods.length == 0) {
            httpMethods = RequestMethod.values();
        }

        final var handlerExecution = new HandlerExecution(controller, method);
        for (final var httpMethod : httpMethods) {
            final var key = new HandlerKey(mapping.value(), httpMethod);
            handlerExecutions.put(key, handlerExecution);
        }
    }
}
