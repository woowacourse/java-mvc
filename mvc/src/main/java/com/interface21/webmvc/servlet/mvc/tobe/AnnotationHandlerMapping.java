package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
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
        try {
            Set<Class<?>> controllerClasses = getControllersWithBasePackage();

            for (Class<?> controller : controllerClasses) {
                Object handler = controller.getConstructor().newInstance();

                for (Method method : controller.getMethods()) {
                    registerHandlerExecutions(method, handler);
                }
            }
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
            log.error("Handler 매핑 중 오류가 발생했습니다.");
            throw new RuntimeException(e);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandlerExecutions(Method method, Object handler) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        if (annotation == null) {
            return;
        }

        String value = annotation.value();
        RequestMethod[] requestMethods = annotation.method();

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(value, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(handler, method);
            handlerExecutions.put(handlerKey, handlerExecution);
            log.info("{}: {}", handlerKey, handlerExecution);
        }
    }

    private Set<Class<?>> getControllersWithBasePackage() {
        Reflections reflections = new Reflections(this.basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));

        return handlerExecutions.get(handlerKey);
    }
}
