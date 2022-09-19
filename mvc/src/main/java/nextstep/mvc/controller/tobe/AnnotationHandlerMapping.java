package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.exception.HandlerMappingException;
import nextstep.mvc.exception.ReflectionException;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        for (final Class<?> controllerClass : getClassesWithControllerAnnotation()) {
            addHandlerExecutionsFromControllerClass(controllerClass);
        }

        log.info("Initialized Handler Mapping!");
        handlerExecutions.keySet()
                .forEach(key -> log.info("{}, {}", key.toString(), handlerExecutions.get(key).toString()));
    }

    private Set<Class<?>> getClassesWithControllerAnnotation() {
        final Reflections reflections = new Reflections(basePackages);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void addHandlerExecutionsFromControllerClass(final Class<?> controllerClass) {
        final Constructor<?> constructor = getConstructor(controllerClass);
        final Object instance = getNewInstance(constructor);
        for (final Method method : controllerClass.getMethods()) {
            addHandlerExecutionsIfRequestMappingAnnotation(instance, method);
        }
    }

    private Constructor<?> getConstructor(final Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor();
        } catch (final NoSuchMethodException e) {
            throw new ReflectionException("A matching method is not found.", e);
        }
    }

    private Object getNewInstance(final Constructor<?> constructor) {
        try {
            return constructor.newInstance();
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ReflectionException("Failed to create and initialize a new instance.", e);
        }
    }

    private void addHandlerExecutionsIfRequestMappingAnnotation(final Object instance, final Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            addHandlerExecutions(instance, method);
        }
    }

    private void addHandlerExecutions(final Object instance, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String url = requestMapping.value();
        for (final RequestMethod requestMethod : requestMapping.method()) {
            handlerExecutions.put(new HandlerKey(url, requestMethod), new HandlerExecution(instance, method));
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
            throw new HandlerMappingException("A matching handler is not found.");
        }
    }
}
