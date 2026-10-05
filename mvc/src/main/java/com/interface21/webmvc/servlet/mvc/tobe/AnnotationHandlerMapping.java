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

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final var reflections = new Reflections(basePackage);
        for (final var controllerClass : reflections.getTypesAnnotatedWith(Controller.class)) {
            final var controller = createController(controllerClass);
            for (final var method : controllerClass.getDeclaredMethods()) {
                registerHandler(controller, method);
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Object createController(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러를 생성할 수 없습니다: " + controllerClass.getName(), e);
        }
    }

    private void registerHandler(final Object controller, final Method method) {
        final var requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping == null) {
            return;
        }
        final var handlerExecution = new HandlerExecution(controller, method);
        for (final var requestMethod : getRequestMethods(requestMapping)) {
            handlerExecutions.put(new HandlerKey(requestMapping.value(), requestMethod), handlerExecution);
        }
    }

    private RequestMethod[] getRequestMethods(final RequestMapping requestMapping) {
        if (requestMapping.method().length == 0) {
            return RequestMethod.values();
        }
        return requestMapping.method();
    }

    public Object getHandler(final HttpServletRequest request) {
        final var requestMethod = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), requestMethod));
    }
}
