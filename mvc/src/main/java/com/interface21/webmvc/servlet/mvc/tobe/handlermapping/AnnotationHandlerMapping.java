package com.interface21.webmvc.servlet.mvc.tobe.handlermapping;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
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

    public void initialize() {
        try {
            scanControllers();
            log.info("Initialized AnnotationHandlerMapping!");

        } catch (Exception e) {
            log.error("an error occurred while scanning handler mappings.", e);
            throw new RuntimeException(e);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final var requestURI = request.getRequestURI();
        final var requestMethod = RequestMethod.of(request.getMethod());
        final var handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }

    private void scanControllers() throws Exception {
        final var scanner = new ControllerScanner(basePackage);
        final var controllers = scanner.getControllers();

        for (final var clazz : controllers.keySet()) {
            scanControllerMethods(clazz, controllers.get(clazz));
        }
    }

    private void scanControllerMethods(final Class<?> clazz, final Object controller) throws Exception {
        for (final var method : clazz.getMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                addHandlerMappings(controller, method);
            }
        }
    }

    private void addHandlerMappings(final Object controller, final Method handlerMethod) throws Exception {
        final var requestMapping = handlerMethod.getAnnotation(RequestMapping.class);
        final var requestMethods = scanRequestMethods(requestMapping);

        for (final var requestMethod : requestMethods) {
            final var handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            final var handlerExecution = new HandlerExecution(controller, handlerMethod);
            handlerExecutions.putIfAbsent(handlerKey, handlerExecution);
        }
    }

    private RequestMethod[] scanRequestMethods(final RequestMapping requestMapping) {
        if (requestMapping.method().length == 0) {
            return RequestMethod.values();
        }
        return requestMapping.method();
    }

}
