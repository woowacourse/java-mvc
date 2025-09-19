package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final String[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final String... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        final Map<Class<?>, Object> controllers = ControllerScanner.scan(basePackage);
        registerHandlers(controllers);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandlers(final Map<Class<?>, Object> controllers) {
        for (final Entry<Class<?>, Object> entry : controllers.entrySet()) {
            registerHandlersForController(entry.getKey(), entry.getValue());
        }
    }

    private void registerHandlersForController(final Class<?> controllerClass, final Object controllerInstance) {
        final Set<Method> requestMappingMethods = getRequestMappingMethods(controllerClass);

        for (final Method method : requestMappingMethods) {
            registerHandlersForMethod(controllerInstance, method);
        }
    }

    private Set<Method> getRequestMappingMethods(final Class<?> controllerClass) {
        return ReflectionUtils.getAllMethods(
                controllerClass,
                ReflectionUtils.withAnnotation(RequestMapping.class)
        );
    }

    private void registerHandlersForMethod(final Object controllerInstance, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String url = requestMapping.value();
        final RequestMethod[] requestMethods = getRequestMethods(requestMapping);

        for (final RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private RequestMethod[] getRequestMethods(final RequestMapping requestMapping) {
        final RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }


    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String httpMethod = request.getMethod();

        final RequestMethod requestMethod = RequestMethod.getBy(httpMethod);
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
