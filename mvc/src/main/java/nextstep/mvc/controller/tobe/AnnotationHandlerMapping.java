package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
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
        final Set<Class<?>> controllerClasses = getControllerClasses();
        for (final Class<?> controllerClass : controllerClasses) {
            addHandlersFromControllerClass(controllerClass);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlersFromControllerClass(final Class<?> controllerClass) {
        final Constructor<?> constructor = getConstructor(controllerClass);
        final Object instance = getNewInstance(constructor);
        for (final Method method : controllerClass.getMethods()) {
            addHandlerIfRequestMappingAnnotation(instance, method);
        }
    }

    private Set<Class<?>> getControllerClasses() {
        final Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private Constructor<?> getConstructor(final Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor();
        } catch (final NoSuchMethodException e) {
            throw new IllegalArgumentException("Reflection: A matching method is not found.");
        }
    }

    private Object getNewInstance(final Constructor<?> constructor) {
        try {
            return constructor.newInstance();
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Reflection: Failed to create and initialize a new instance.");
        }
    }

    private void addHandlerIfRequestMappingAnnotation(final Object instance, final Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            addHandlers(instance, method);
        }
    }

    private void addHandlers(final Object instance, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String url = requestMapping.value();
        for (final RequestMethod httpMethod : requestMapping.method()) {
            handlerExecutions.put(new HandlerKey(url, httpMethod), new HandlerExecution(instance, method));
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = HandlerKey.from(request);
        final HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);
        checkNullOfHandler(handlerExecution);
        return handlerExecution;
    }

    private void checkNullOfHandler(final HandlerExecution handlerExecution) {
        if (handlerExecution == null) {
            throw new IllegalArgumentException("A matching handler is not found.");
        }
    }
}
