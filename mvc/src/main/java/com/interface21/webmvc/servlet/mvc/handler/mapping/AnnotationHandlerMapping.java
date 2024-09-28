package com.interface21.webmvc.servlet.mvc.handler.mapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.handler.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.handler.HandlerKey;

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
        log.info("Initialized AnnotationHandlerMapping!");
        Map<Class<?>, Object> controllers = getControllers();
        for (Class<?> controllerClass : controllers.keySet()) {
            MethodScanner methodScanner = new MethodScanner(controllerClass);
            Set<Method> methods = methodScanner.findAllByAnnotation(RequestMapping.class);
            methods.forEach(method -> addHandlerExecution(controllers.get(controllerClass), method));
        }
    }

    private Map<Class<?>, Object> getControllers() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        controllerScanner.initialize();

        return controllerScanner.getControllers();
    }

    private void addHandlerExecution(Object controller, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        RequestMappingResolver requestMappingResolver = new RequestMappingResolver(requestMapping);
        String url = requestMappingResolver.getUrl();
        for (RequestMethod requestMethod : requestMappingResolver.getRequestMethods()) {
            handlerExecutions.put(new HandlerKey(url, requestMethod), new HandlerExecution(controller, method));
        }
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
