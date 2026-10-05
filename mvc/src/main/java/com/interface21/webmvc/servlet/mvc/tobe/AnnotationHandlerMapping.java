package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.core.util.ReflectionUtils;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
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
        final ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        controllers.forEach(this::registerHandlers);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandlers(Class<?> clazz, Object controller) {
        Set<Method> methods = ReflectionUtils.getDeclaredMethods(
                clazz,
                ReflectionUtils.withAnnotation(RequestMapping.class)
        );
        for (Method method : methods) {
            registerHandlerMethod(method, controller);
        }
    }

    private void registerHandlerMethod(Method method, Object controller) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        ReflectionUtils.makeAccessible(method);
        String value = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(value, requestMethod);
            if (handlerExecutions.containsKey(handlerKey)) {
                throw new IllegalArgumentException("Duplicated mapping: " + handlerKey);
            }
            handlerExecutions.put(
                    handlerKey,
                    new HandlerExecution(controller, method)
            );
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String uri = request.getRequestURI();
        final RequestMethod requestMethod;
        try {
            String method = request.getMethod();
            requestMethod = RequestMethod.valueOf(method);
        } catch (IllegalArgumentException e) {
            return null;
        }
        return handlerExecutions.get(new HandlerKey(uri, requestMethod));
    }
}
