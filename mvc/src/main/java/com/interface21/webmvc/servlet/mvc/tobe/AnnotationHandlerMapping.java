package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
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
        for (final Object basePackage : basePackages) {
            final Set<Class<?>> controllers = scanControllers(basePackage);
            for (final Class<?> controller : controllers) {
                final Method[] requestMappingMethods = getRequestMappingMethods(controller);
                for (final Method method : requestMappingMethods) {
                    final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                    final String url = annotation.value();
                    for (final RequestMethod requestMethod : resolveRequestMethods(annotation)) {
                        registerHandler(new HandlerKey(url, requestMethod), new HandlerExecution());
                    }
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

    private Set<Class<?>> scanControllers(final Object basePackage) {
        final Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private Method[] getRequestMappingMethods(final Class<?> controller) {
        return Arrays.stream(controller.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(RequestMapping.class))
                .toArray(Method[]::new);
    }

    private RequestMethod[] resolveRequestMethods(final RequestMapping annotation) {
        final RequestMethod[] methods = annotation.method();
        return (methods.length == 0) ? RequestMethod.values() : methods;
    }

    private void registerHandler(final HandlerKey key, final HandlerExecution execution) {
        handlerExecutions.put(key, execution);
    }
}
