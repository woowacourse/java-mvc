package webmvc.org.springframework.web.servlet.mvc;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);


    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackages = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final var controllers = getControllers();

        for (final var controller : controllers) {
            Map<HandlerKey, HandlerExecution> keyToExecutions = findHandlersFrom(controller);
            handlerExecutions.putAll(keyToExecutions);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private List<Class<?>> getControllers() {
        return Arrays.stream(basePackages)
                .flatMap(basePackage -> new Reflections(basePackage)
                        .getTypesAnnotatedWith(Controller.class)
                        .stream())
                .collect(Collectors.toList());
    }

    private Map<HandlerKey, HandlerExecution> findHandlersFrom(final Class<?> controller) {
        return Arrays.stream(controller.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .flatMap(this::getHandlerKeyToExecutions)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Stream<Map.Entry<HandlerKey, HandlerExecution>> getHandlerKeyToExecutions(final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final RequestMethod[] httpMethods = requestMapping.method();
        return Stream.of(httpMethods)
                .map(httpMethod -> {
                    HandlerKey key = new HandlerKey(requestMapping.value(), httpMethod);
                    HandlerExecution handlerExecution = new HandlerExecution(method);
                    return Map.entry(key, handlerExecution);
                });
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        final var handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }

}
