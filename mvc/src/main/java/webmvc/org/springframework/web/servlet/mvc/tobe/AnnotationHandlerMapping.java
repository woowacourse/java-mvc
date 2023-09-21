package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final var reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (final var clazz : controllerClasses) {
            addHandlerExecutionsInClazz(clazz);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerExecutionsInClazz(final Class<?> clazz) {
        final List<Method> methods = findMethodsAnnotatedRequestMapping(clazz);
        for (final var method : methods) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            addHandlerExecution(method, requestMapping);
        }
    }

    private List<Method> findMethodsAnnotatedRequestMapping(final Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(it -> it.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private void addHandlerExecution(final Method method, final RequestMapping requestMapping) {
        final String url = requestMapping.value();
        final RequestMethod[] requestMethods = requestMapping.method();
        final var handlerExecution = new HandlerExecution(method);
        for (final var requestMethod : requestMethods) {
            final var handlerKey = new HandlerKey(url, requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final var requestMethod = RequestMethod.valueOf(request.getMethod());
        final var handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
