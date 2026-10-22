package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        controllerScanner.getControllers().forEach(this::registerHandlers);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandlers(final Class<?> controllerType, final Object controller) {
        for (final Method method : controllerType.getDeclaredMethods()) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping == null) {
                continue;
            }

            final RequestMethod[] requestMethods = requestMapping.method().length == 0
                    ? RequestMethod.values() : requestMapping.method();
            final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            for (final RequestMethod requestMethod : requestMethods) {
                final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
                if (handlerExecutions.putIfAbsent(handlerKey, handlerExecution) != null) {
                    throw new IllegalStateException("중복된 요청 매핑입니다: " + handlerKey);
                }
            }
        }
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        final String url = request.getRequestURI().substring(request.getContextPath().length());
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(url, requestMethod));
    }
}
