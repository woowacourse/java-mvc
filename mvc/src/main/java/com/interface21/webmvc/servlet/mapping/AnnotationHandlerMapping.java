package com.interface21.webmvc.servlet.mapping;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.asis.tobe.ControllerScanner;
import com.interface21.webmvc.servlet.mvc.asis.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.asis.tobe.HandlerKey;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    public void initialize() {
        try {
            ControllerScanner controllerScanner = new ControllerScanner(basePackage);
            Map<Class<?>, Object> controllers = controllerScanner.getControllers();

            Set<Method> requestMappingMethods = getRequestMappingMethods(controllers.keySet());

            for (Method method : requestMappingMethods) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    addHandlerExecutions(controllers, method, requestMapping);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerExecutions(Map<Class<?>, Object> controllers, Method method, RequestMapping requestMapping) {
        String url = requestMapping.value();
        RequestMethod[] methods = requestMapping.method();

        Class<?> declaringClass = method.getDeclaringClass();
        Object controller = controllers.get(declaringClass);

        for (HandlerKey handlerKey : mapHandlerKeys(url, methods)) {
            HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private Set<Method> getRequestMappingMethods(Set<Class<?>> controllers) {
        Set<Method> methods = new HashSet<>();
        for (Class<?> controllerTypes : controllers) {
            Method[] declaredMethods = controllerTypes.getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    methods.add(method);
                }
            }
        }
        return methods;
    }

    private List<HandlerKey> mapHandlerKeys(String uri, RequestMethod[] methods) {
        List<HandlerKey> handlerKeys = new ArrayList<>();
        for (RequestMethod method : methods) {
            handlerKeys.add(new HandlerKey(uri, method));
        }
        return handlerKeys;
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(requestUri, RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }
}
