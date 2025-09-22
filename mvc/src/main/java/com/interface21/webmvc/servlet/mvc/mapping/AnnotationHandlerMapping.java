package com.interface21.webmvc.servlet.mvc.mapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.ControllerScanner;
import com.interface21.webmvc.servlet.mvc.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.HandlerKey;

import jakarta.servlet.http.HttpServletRequest;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final String APP_CONTROLLER_PACKAGE = "com.techcourse";

    private final Object[] basePackages;
    private final ControllerScanner controllerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.controllerScanner = new ControllerScanner(basePackages, APP_CONTROLLER_PACKAGE);
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        controllers.forEach(this::registerHandler);
    }

    private void registerHandler(final Class<?> clazz, final Object handler) {
        Set<Method> methods = ReflectionUtils.getAllMethods(clazz,
            ReflectionUtils.withAnnotation(RequestMapping.class));
        for (Method method : methods) {
            registerRequestMappingMethod(handler, method);
        }
    }

    private void registerRequestMappingMethod(final Object handler, final Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String uri = requestMapping.value();
        RequestMethod[] requestMethods = getRequestMethods(requestMapping);

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey key = new HandlerKey(uri, requestMethod);
            HandlerExecution execution = new HandlerExecution(handler, method);
            handlerExecutions.put(key, execution);
        }
    }

    private RequestMethod[] getRequestMethods(final RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.from(request.getMethod());
        HandlerKey key = new HandlerKey(uri, requestMethod);

        return handlerExecutions.get(key);
    }
}
