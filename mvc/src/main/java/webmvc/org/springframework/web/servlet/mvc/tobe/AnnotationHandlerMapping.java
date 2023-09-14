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

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        final Set<Class<?>> controllerClasses = new Reflections(basePackage).getTypesAnnotatedWith(Controller.class);
        for (final Class<?> controllerClass : controllerClasses) {
            final List<Method> methods = getMethods(controllerClass);
            putHandlerEachMethod(methods);
        }
    }

    private List<Method> getMethods(final Class<?> controllerClass) {
        return Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private void putHandlerEachMethod(final List<Method> methods) {
        for (final Method method : methods) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            final String url = requestMapping.value();
            final RequestMethod[] requestMethods = requestMapping.method();
            for (final RequestMethod requestMethod : requestMethods) {
                final HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                handlerExecutions.put(handlerKey, new HandlerExecution(method));
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final var handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
