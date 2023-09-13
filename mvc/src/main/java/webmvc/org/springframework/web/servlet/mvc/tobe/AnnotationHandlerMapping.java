package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        for (final Object base : basePackage) {
            initializeHandlerInPackage(base);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initializeHandlerInPackage(final Object base) {
        final Reflections reflections = new Reflections(base);
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        for (final Class<?> clazz : classes) {
            initializeHandlerInClass(clazz);
        }
    }

    private void initializeHandlerInClass(final Class<?> clazz) {
        final Method[] methods = clazz.getDeclaredMethods();
        for (final Method method : methods) {
            initializeHandlerByMethod(clazz, method);
        }
    }

    private void initializeHandlerByMethod(final Class<?> clazz, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            initializeRequestMappingHandler(clazz, method, requestMapping);
        }
    }

    private void initializeRequestMappingHandler(
            final Class<?> clazz,
            final Method method,
            final RequestMapping requestMapping
    ) {
        final String url = requestMapping.value();
        final RequestMethod[] requestMethods = requestMapping.method();
        for (final RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(clazz, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String url = request.getRequestURI();
        final String requestMethod = request.getMethod();

        final HandlerKey handlerKey = new HandlerKey(url, RequestMethod.valueOf(requestMethod.toUpperCase()));

        return handlerExecutions.get(handlerKey);
    }
}
