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

    public void initialize() throws Exception {
        log.info("Initialized AnnotationHandlerMapping!");
        initializeHandlerExecutions();
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        if (!handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException("요청 ["
                    + requestMethod + " " + requestURI + "] 에 매핑된 핸들러가 없습니다.");
        }

        return handlerExecutions.get(handlerKey);
    }

    private void initializeHandlerExecutions() throws Exception {
        Set<Class<?>> controllersInPackage = getControllersInPackage();

        for (Class<?> controller : controllersInPackage) {
            registerHandlerMethods(controller, controller.getMethods());
        }
    }

    private Set<Class<?>> getControllersInPackage() {
        Reflections reflections = new Reflections(this.basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void registerHandlerMethods(Class<?> controller, Method[] methods) throws Exception {
        for (Method method : controller.getMethods()) {
            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            if (annotation == null) {
                continue;
            }
            registerHandlerMethod(controller, method, annotation);
        }
    }

    private void registerHandlerMethod(Class<?> controller, Method method, RequestMapping annotation)
            throws Exception {
        RequestMethod[] requestMethods = annotation.method();
        String uri = annotation.value();

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
            Object handler = controller.getConstructor().newInstance();
            HandlerExecution handlerExecution = new HandlerExecution(handler, method);

            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }
}
