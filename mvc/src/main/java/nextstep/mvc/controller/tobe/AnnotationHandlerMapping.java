package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        final Reflections reflections = new Reflections(basePackage);
        reflections.getTypesAnnotatedWith(Controller.class)
                .forEach(this::addAnnotatedMethodToHandlerExecutions);
    }

    private void addAnnotatedMethodToHandlerExecutions(final Class<?> clazz) {
        try {
            final Object instance = clazz.getConstructor().newInstance();
            findAnnotatedMethod(clazz)
                    .forEach(method -> addToHandlerExecutions(instance, method));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            log.warn("Fail to instantiate controller class.", e);
        }
    }

    private void addToHandlerExecutions(Object instance, Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        HandlerKey.createFrom(requestMapping)
                .forEach(handlerKey -> handlerExecutions.put(handlerKey, new HandlerExecution(method, instance)));
    }

    private List<Method> findAnnotatedMethod(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey key = new HandlerKey(request.getRequestURI(), requestMethod);
        return handlerExecutions.get(key);
    }
}
