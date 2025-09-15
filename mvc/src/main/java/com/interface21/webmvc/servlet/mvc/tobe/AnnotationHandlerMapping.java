package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        final Reflections reflections = new Reflections(basePackage);
        scanControllers(reflections);
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.from(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }

    private void scanControllers(final Reflections reflections) {
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : controllers) {
            scanRequestMappingMethods(controller);
        }
    }

    private void scanRequestMappingMethods(final Class<?> controller) {
        final Method[] methods = controller.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                final RequestMapping mapping = method.getDeclaredAnnotation(RequestMapping.class);
                final RequestMethod[] requestMethods = mapping.method();
                addHandlerExecutions(controller, method, requestMethods, mapping);
            }
        }
    }

    private void addHandlerExecutions(final Class<?> controller, final Method method,
                                      final RequestMethod[] requestMethods, final RequestMapping mapping) {
        for (RequestMethod requestMethod : requestMethods) {
            addHandlerExecution(controller, method, mapping, requestMethod);
        }
    }

    private void addHandlerExecution(final Class<?> controller, final Method method, final RequestMapping mapping,
                                     final RequestMethod requestMethod) {
        try {
            final HandlerKey handlerKey = new HandlerKey(mapping.value(), requestMethod);
            final Object controllerInstance = controller.getDeclaredConstructor().newInstance();
            final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        } catch (Exception e) {
            log.warn("controller 인스턴스 생성 실패");
        }
    }
}
