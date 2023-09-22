package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.exception.GetInstanceException;

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
        for (Object targetPackage : basePackage) {
            final Reflections reflections = new Reflections(targetPackage);
            final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
            addAllHandlerExecutions(controllerClasses);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addAllHandlerExecutions(final Set<Class<?>> controllerClasses) {
        for (final Class<?> controllerClass : controllerClasses) {
            addHandlerExecutions(controllerClass);
        }
    }

    private void addHandlerExecutions(final Class<?> controllerClass) {
        final Method[] methods = controllerClass.getMethods();
        Arrays.stream(methods)
            .filter(method -> method.isAnnotationPresent(RequestMapping.class))
            .forEach(method -> {
                final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method()[0]);
                final HandlerExecution handlerExecution = new HandlerExecution(getInstance(controllerClass), method);
                handlerExecutions.put(handlerKey, handlerExecution);
            });
    }

    private Object getInstance(final Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (InvocationTargetException
                 | InstantiationException
                 | IllegalAccessException
                 | NoSuchMethodException e) {
            throw new GetInstanceException();
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
