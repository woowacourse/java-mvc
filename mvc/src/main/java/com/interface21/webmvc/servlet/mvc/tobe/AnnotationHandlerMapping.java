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

        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllerClasses) {
            try {
                final Object controller = controllerClass.getDeclaredConstructor().newInstance();
                final Method[] controllerDeclaredMethods = controllerClass.getDeclaredMethods();
                putHandlerExecutions(controller, controllerDeclaredMethods);
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Controller 초기화에 실패했습니다: " + controllerClass.getName(), e);
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerExecution handlerExecution = handlerExecutions.get(
            new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));

        if (handlerExecution == null) {
            return handlerExecutions.get(new HandlerKey(request.getRequestURI(), RequestMethod.ANY));
        }

        return handlerExecution;
    }

    private void putHandlerExecutions(Object controller, Method[] controllerDeclaredMethods) {
        for (Method controllerMethod : controllerDeclaredMethods) {
            if (!controllerMethod.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }

            RequestMapping requestMapping = controllerMethod.getAnnotation(RequestMapping.class);

            if (!existsRequestMethod(requestMapping)) {
                HandlerKey handlerKey = new HandlerKey(requestMapping.value(), RequestMethod.ANY);
                HandlerExecution handlerExecution = new HandlerExecution(controller, controllerMethod);
                handlerExecutions.put(handlerKey, handlerExecution);
                continue;
            }

            for (RequestMethod requestMethod : requestMapping.method()) {
                HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
                HandlerExecution handlerExecution = new HandlerExecution(controller, controllerMethod);
                handlerExecutions.put(handlerKey, handlerExecution);
            }
        }
    }

    private boolean existsRequestMethod(final RequestMapping requestMapping) {
        return requestMapping.method().length != 0;
    }

}
