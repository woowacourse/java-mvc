package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.handler.HandlerMapping;

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
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> handlerTypes = reflections.getTypesAnnotatedWith(Controller.class);
        for (final Class<?> handlerType : handlerTypes) {
            processHandlerType(handlerType);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void processHandlerType(final Class<?> handlerType) {
        final Method[] handlerMethods = handlerType.getDeclaredMethods();

        for (final Method handlerMethod : handlerMethods) {
            if (handlerMethod.isAnnotationPresent(RequestMapping.class)) {
                final HandlerExecution handlerExecution = getHandlerExecution(handlerType, handlerMethod);
                registerMapping(handlerExecution, handlerMethod);
            }
        }
    }

    private HandlerExecution getHandlerExecution(final Class<?> handlerType,
                                                 final Method handlerMethod) {
        try {
            final Object handler = handlerType.getConstructor().newInstance();
            return new HandlerExecution(handler, handlerMethod);

        } catch (final InstantiationException |
                       IllegalAccessException |
                       InvocationTargetException |
                       NoSuchMethodException e) {

            throw new RuntimeException(e);
        }
    }

    private void registerMapping(final HandlerExecution handlerExecution,
                                 final Method handlerMethod) {
        final RequestMapping requestMapping = handlerMethod.getAnnotation(RequestMapping.class);
        final String mappingUrl = requestMapping.value();
        final RequestMethod[] requestMethods = requestMapping.method();

        for (final RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(mappingUrl, requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String requestUrl = request.getRequestURI();
        final String requestMethod = request.getMethod();
        final HandlerKey handlerKey = new HandlerKey(requestUrl, RequestMethod.from(requestMethod));
        return handlerExecutions.get(handlerKey);
    }
}
