package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletRequest;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final RequestMappingMethodFinder methodFinder = new RequestMappingMethodFinder();
    private final HandlerKeyMapper handlerKeyMapper = new HandlerKeyMapper();

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        controllers.keySet().forEach(controllerClass -> {
            Set<Method> requestMappingMethods = methodFinder.getRequestMappingMethods(Set.of(controllerClass));
            processRequestMappingMethods(controllers, requestMappingMethods);
        });

        log.info("Initialized AnnotationHandlerMapping with controllers: {}", controllers.keySet());
    }

    private void processRequestMappingMethods(Map<Class<?>, Object> controllers, Set<Method> requestMappingMethods) {
        requestMappingMethods.forEach(method -> {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            addHandlerExecutions(controllers, method, requestMapping);
        });
    }

    private void addHandlerExecutions(Map<Class<?>, Object> controllers, Method method, RequestMapping requestMapping) {
        List<HandlerKey> handlerKeys = handlerKeyMapper.mapHandlerKeys(requestMapping.value(), requestMapping.method());
        Object controllerInstance = controllers.get(method.getDeclaringClass());

        for (HandlerKey handlerKey : handlerKeys) {
            handlerExecutions.put(handlerKey, new HandlerExecution(controllerInstance, method));
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.from(request.getMethod());

        HandlerKey handlerKey = new HandlerKey(url, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
