package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.exception.HandlerInitializationException;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
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

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, method);

        return handlerExecutions.get(handlerKey);
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> annotatedControllerTypes = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerType : annotatedControllerTypes) {
            addHandlers(controllerType);
        }
        
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlers(final Class<?> controller) {
        try {
            Object controllerInstance = controller.getConstructor().newInstance();
            Arrays.stream(controller.getMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .forEach(method -> addHandler(method, controllerInstance));
        } catch (Exception e) {
            throw new HandlerInitializationException("핸들러 초기화에서 예외가 발생했습니다.", e);
        }
    }

    private void addHandler(final Method method, final Object controllerInstance) {
        HandlerKey handlerKey = createHandlerKey(method);
        HandlerExecution handlerExecution = createHandlerExecution(method, controllerInstance);
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    private HandlerKey createHandlerKey(final Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        return new HandlerKey(requestMapping.value(), requestMapping.method()[0]);
    }

    private HandlerExecution createHandlerExecution(final Method method, final Object controller) {
        return new HandlerExecution(method, controller);
    }
}
