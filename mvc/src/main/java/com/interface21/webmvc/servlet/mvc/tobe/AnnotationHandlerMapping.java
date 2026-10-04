package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        final ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        for (final Object controller : controllers.values()) {
            final Class<?> clazz = controller.getClass();
            for (final Method method : clazz.getDeclaredMethods()) {
                final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                if (requestMapping == null) {
                    continue;
                }

                final HandlerExecution execution = new HandlerExecution(controller, method);
                for (final RequestMethod requestMethod : getRequestMethods(requestMapping)) {
                    final HandlerKey key = new HandlerKey(requestMapping.value(), requestMethod);
                    final HandlerExecution existing = handlerExecutions.putIfAbsent(key, execution);
                    if (existing != null) {
                        throw new IllegalStateException("중복 핸들러 매핑 오류: " + key);
                    }
                }
            }
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(
                request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod())
        );
        return handlerExecutions.get(handlerKey);
    }

    private static RequestMethod[] getRequestMethods(final RequestMapping requestMapping) {
        final RequestMethod[] configuredMethods = requestMapping.method();
        if (configuredMethods.length == 0) {
            return RequestMethod.values();
        }
        return configuredMethods;
    }
}
