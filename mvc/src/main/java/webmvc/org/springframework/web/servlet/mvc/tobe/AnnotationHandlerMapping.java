package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);


    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackages = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        final var controllers = Arrays.stream(basePackages)
                .flatMap(basePackage -> getControllerFrom(basePackage).stream())
                .map(this::newInstance)
                .collect(Collectors.toList());

        for (Object controller : controllers) {
            Map<HandlerKey, HandlerExecution> keyToExecutions = findHandlersFrom(controller);
            handlerExecutions.putAll(keyToExecutions);
        }
    }

    private Set<Class<?>> getControllerFrom(final Object basePackage) {
        return new Reflections(basePackage).getTypesAnnotatedWith(Controller.class);
    }

    private Map<HandlerKey, HandlerExecution> findHandlersFrom(final Object controller) {
        return Arrays.stream(
                        controller.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .flatMap(method -> getHandlerKeyToExecutions(controller, method))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Object newInstance(final Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<Map.Entry<HandlerKey, HandlerExecution>> getHandlerKeyToExecutions(final Object controller,
                                                                                      final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final RequestMethod[] httpMethods = requestMapping.method();
        return Stream.of(httpMethods)
                .map(httpMethod -> {
                    HandlerKey key = new HandlerKey(requestMapping.value(), httpMethod);
                    HandlerExecution handlerExecution = new HandlerExecution(controller, method);
                    return Map.entry(key, handlerExecution);
                });
    }

    public Object getHandler(final HttpServletRequest request) {
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        final var handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }

}
