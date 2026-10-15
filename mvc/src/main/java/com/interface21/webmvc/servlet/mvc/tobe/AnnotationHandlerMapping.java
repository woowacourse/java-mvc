package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
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

    @Override
    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        for (Method method : getRequestMappingMethods(controllers.keySet())) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            addHandlerExecutions(controllers, method, requestMapping);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        HandlerKey key = new HandlerKey(request.getRequestURI(),
            RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(key);
    }

    private void addHandlerExecutions(final Map<Class<?>, Object> controllers, final Method method,
                                      final RequestMapping requestMapping) {
        Object controller = controllers.get(method.getDeclaringClass());
        for (HandlerKey handlerKey : mapHandlerKeys(requestMapping.value(), requestMapping.method())) {
            validateDuplicateHandlerKey(handlerKey);
            handlerExecutions.put(handlerKey, new HandlerExecution(controller, method));
        }
    }

    private void validateDuplicateHandlerKey(final HandlerKey handlerKey) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalStateException("중복된 요청 매핑이 존재합니다: " + handlerKey);
        }
    }

    private Set<Method> getRequestMappingMethods(final Set<Class<?>> controllerClasses) {
        Set<Method> methods = new HashSet<>();
        for (Class<?> controllerClass : controllerClasses) {
            for (Method method : controllerClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    methods.add(method);
                }
            }
        }
        return methods;
    }

    private List<HandlerKey> mapHandlerKeys(final String url, RequestMethod[] requestMethods) {
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }
        List<HandlerKey> handlerKeys = new ArrayList<>();
        for (RequestMethod requestMethod : requestMethods) {
            handlerKeys.add(new HandlerKey(url, requestMethod));
        }
        return handlerKeys;
    }
}
