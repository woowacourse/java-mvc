package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import core.org.springframework.util.ReflectionUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.handlermapping.HandlerMapping;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Set<Class<?>> typesAnnotatedWith = ReflectionUtils.getTypesAnnotatedWith(basePackage, Controller.class);
        for (Class<?> type : typesAnnotatedWith) {
            List<Method> handlers = ReflectionUtils.getMethodsAnnotatedWith(type, RequestMapping.class);
            putHandlerExecutions(type, handlers);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void putHandlerExecutions(Class<?> type, List<Method> handlers) {
        try {
            Object instance = type.getDeclaredConstructor().newInstance();
            for (Method handler : handlers) {
                putHandlerExecutionOfTypeAnnotatedWithController(handler, instance);
            }
        } catch (Exception e) {
            log.error("Failed to initialize controller: {}", type.getSimpleName());
            throw new IllegalStateException(e);
        }
    }

    private void putHandlerExecutionOfTypeAnnotatedWithController(Method handler, Object instance) {
        RequestMapping requestMapping = handler.getAnnotation(RequestMapping.class);
        HandlerExecution handlerExecution = new HandlerExecution(handler, instance);

        log.info("Path: {}, Controller: {}", requestMapping.value(), instance.getClass().getName());

        Arrays.stream(requestMapping.method())
              .map(httpMethod -> new HandlerKey(requestMapping.value(), httpMethod))
              .forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
    }

    @Override
    public Optional<Object> getHandler(final HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);

        return Optional.ofNullable(handlerExecutions.get(handlerKey));
    }

    public Map<Object, Object> getHandlerExecutions() {
        return Map.copyOf(handlerExecutions);
    }
}
