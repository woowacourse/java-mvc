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

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final ControllerScanner controllerScanner = new ControllerScanner(basePackages);
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        for (Entry<Class<?>, Object> controllerEntry : controllers.entrySet()) {
            final Object target = controllerEntry.getValue();
            final Set<Method> requestMappingMethods = getRequestMappingMethods(controllerEntry.getKey());
            for (final Method method : requestMappingMethods) {
                final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                final String url = annotation.value();
                for (final RequestMethod requestMethod : resolveRequestMethods(annotation)) {
                    final HandlerKey key = new HandlerKey(url, requestMethod);
                    checkHandlerIsAlreadyRegistered(key);
                    registerHandler(key, new HandlerExecution(target, method));
                }
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(requestURI, requestMethod));
    }

    private Set<Method> getRequestMappingMethods(final Class<?> controller) {
        return ReflectionUtils.getAllMethods(controller, ReflectionUtils.withAnnotation(RequestMapping.class));
    }

    private RequestMethod[] resolveRequestMethods(final RequestMapping annotation) {
        final RequestMethod[] methods = annotation.method();
        return (methods.length == 0) ? RequestMethod.values() : methods;
    }

    private void registerHandler(final HandlerKey key, final HandlerExecution execution) {
        handlerExecutions.put(key, execution);
    }

    private void checkHandlerIsAlreadyRegistered(HandlerKey key) {
        if (handlerExecutions.containsKey(key)) {
            throw new IllegalArgumentException(
                    "Duplicate mapping found for " + key.getUrl() + " " + key.getRequestMethod());
        }
    }
}
