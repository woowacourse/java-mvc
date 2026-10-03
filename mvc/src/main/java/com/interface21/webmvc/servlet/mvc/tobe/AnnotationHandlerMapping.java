package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        handlerExecutions.clear();

        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllerClasses) {
            registerController(controllerClass);
        }

        log.info("Initialized AnnotationHandlerMapping with {} handlers!", handlerExecutions.size());
    }

    private void registerController(final Class<?> controllerClass) {
        try {
            final Object controller = controllerClass.getDeclaredConstructor().newInstance();
            for (Method method : controllerClass.getDeclaredMethods()) {
                final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                if (requestMapping == null) {
                    continue;
                }

                RequestMethod[] requestMethods = requestMapping.method();
                if (requestMethods.length == 0) {
                    requestMethods = RequestMethod.values();
                }
                for (RequestMethod requestMethod : requestMethods) {
                    final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
                    final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
                    final HandlerExecution existingHandler = handlerExecutions.putIfAbsent(handlerKey, handlerExecution);
                    if (existingHandler != null) {
                        throw new IllegalStateException("중복된 핸들러 매핑입니다: " + handlerKey
                                + ", Controller: " + controllerClass.getName() + "#" + method.getName());
                    }
                    log.info("Mapped {} {} to {}.{}", requestMethod, requestMapping.value(),
                            controllerClass.getSimpleName(), method.getName());
                }
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러 초기화에 실패했습니다: " + controllerClass.getName(), e);
        }
    }

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
