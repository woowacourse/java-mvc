package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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
        final Map<Class<?>, Object> controllers = new ControllerScanner(basePackage).getControllers();
        controllers.forEach(this::registerController);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        try {
            RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
            return handlerExecutions.get(new HandlerKey(url, requestMethod));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private void registerController(final Class<?> controllerClass, final Object controller) {
        for (final Method method : controllerClass.getMethods()) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping != null) {
                registerHandler(controller, method, requestMapping);
            }
        }
    }

    private void registerHandler(final Object controller, final Method method, final RequestMapping requestMapping) {
        final HandlerExecution execution = new HandlerExecution(method, controller);
        for (final RequestMethod requestMethod : readRequestMethods(requestMapping)) {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            validateNotDuplicated(handlerKey, method);
            handlerExecutions.put(handlerKey, execution);
        }
    }

    private void validateNotDuplicated(final HandlerKey handlerKey, final Method method) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalStateException(
                    "중복된 매핑입니다. key: %s, method: %s".formatted(handlerKey, method));
        }
    }

    private RequestMethod[] readRequestMethods(RequestMapping requestMapping) {
        if(requestMapping.method().length == 0) {
            return RequestMethod.values();
        }

        return requestMapping.method();
    }
}
