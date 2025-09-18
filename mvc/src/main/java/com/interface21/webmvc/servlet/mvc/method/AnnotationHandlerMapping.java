package com.interface21.webmvc.servlet.mvc.method;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final String basePackage;
    private final ControllerScanner controllerScanner;
    private final HandlerMappingRegistry handlerRegistry;

    public AnnotationHandlerMapping(final String basePackage) {
        this.basePackage = basePackage;
        this.controllerScanner = new ControllerScanner(basePackage);
        this.handlerRegistry = new HandlerMappingRegistry();
    }

    public void initialize() {
        log.info("Initializing AnnotationHandlerMapping for package: {}", basePackage);

        final Map<Class<?>, Object> controllers = controllerScanner.scanAndCreateControllers();

        if (controllers.isEmpty()) {
            log.warn("No @Controller classes found in package: {}", basePackage);
            return;
        }

        handlerRegistry.registerControllers(controllers);

        log.info("AnnotationHandlerMapping initialization completed. Total handlers: {}",
                handlerRegistry.size());
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = createHandlerKey(request);

        if (handlerKey == null) {
            log.debug("Could not create HandlerKey for request: {} {}",
                    request.getMethod(), request.getRequestURI());
            return null;
        }

        final HandlerExecution handlerExecution = handlerRegistry.getHandlerExecution(handlerKey);

        if (handlerExecution == null) {
            log.debug("No handler found for: {}", handlerKey);
        }

        return handlerExecution;
    }

    private HandlerKey createHandlerKey(final HttpServletRequest request) {
        final String uri = request.getRequestURI();
        final String httpMethodName = request.getMethod();

        try {
            final RequestMethod requestMethod = RequestMethod.valueOf(httpMethodName);
            return new HandlerKey(uri, requestMethod);
        } catch (final IllegalArgumentException e) {
            log.warn("Unsupported HTTP method: {}", httpMethodName);
            return null;
        }
    }
}
