package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
        final ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        controllers.forEach(this::registerHandlerExecutions);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandlerExecutions(final Class<?> controllerClass, final Object controller) {
        for (final Method method : controllerClass.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }
            validatePublicMethod(method);
            registerHandlerExecution(controller, method);
        }
    }

    private void validatePublicMethod(final Method method) {
        if (!Modifier.isPublic(method.getModifiers())) {
            throw new IllegalStateException("@RequestMapping 메서드는 public이어야 합니다: " + method);
        }
    }

    private void registerHandlerExecution(final Object controller, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        for (final RequestMethod requestMethod : resolveRequestMethods(requestMapping)) {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            validateDuplicateMapping(handlerKey, method);
            handlerExecutions.put(handlerKey, handlerExecution);
            log.info("Path : {}, Method : {}, Handler : {}", requestMapping.value(), requestMethod, method);
        }
    }

    private void validateDuplicateMapping(final HandlerKey handlerKey, final Method method) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalStateException("중복된 요청 매핑입니다: " + handlerKey + ", " + method);
        }
    }

    private RequestMethod[] resolveRequestMethods(final RequestMapping requestMapping) {
        final RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey key = new HandlerKey(request.getRequestURI(), requestMethod);
        return handlerExecutions.get(key);
    }
}
