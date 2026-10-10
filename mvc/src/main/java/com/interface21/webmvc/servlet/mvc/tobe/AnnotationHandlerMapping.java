package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        handlerExecutions.clear();

        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        controllers.forEach(this::registerController);

        log.info("Initialized AnnotationHandlerMapping with {} handlers!", handlerExecutions.size());
    }

    private void registerController(final Class<?> controllerClass, final Object controller) {
        for (Method method : controllerClass.getDeclaredMethods()) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

            if (requestMapping == null) {
                continue;
            }

            if (!Modifier.isPublic(method.getModifiers())) {
                throw new IllegalStateException("요청 매핑 메서드는 public이어야 합니다: "
                        + controllerClass.getName() + "#" + method.getName());
            }
            registerHandler(controller, method, requestMapping);
        }
    }

    private RequestMethod[] determineRequestMethods(final RequestMapping requestMapping) {
        final RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }

    private void registerHandler(final Object controller, final Method method, final RequestMapping requestMapping) {
        final RequestMethod[] requestMethods = determineRequestMethods(requestMapping);
        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);

        for (RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            final HandlerExecution existingHandler = handlerExecutions.putIfAbsent(handlerKey, handlerExecution);
            if (existingHandler != null) {
                throw new IllegalStateException("중복된 핸들러 매핑입니다: " + handlerKey
                        + ", 컨트롤러: " + method.getDeclaringClass().getName() + "#" + method.getName());
            }
            log.info("Mapped {} {} to {}.{}", requestMethod, requestMapping.value(),
                    method.getDeclaringClass().getSimpleName(), method.getName());
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod;
        try {
            requestMethod = RequestMethod.valueOf(request.getMethod());
        } catch (IllegalArgumentException e) {
            return null;
        }

        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
