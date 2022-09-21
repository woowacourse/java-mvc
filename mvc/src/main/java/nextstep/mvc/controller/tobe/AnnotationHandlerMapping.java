package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
        new Reflections(basePackage)
                .getTypesAnnotatedWith(Controller.class)
                .stream()
                .filter(this::hasMethodAnnotatedRequestMapping)
                .map(this::newInstance)
                .flatMap(this::createRequestMappings)
                .collect(Collectors.toList())
                .forEach(entry -> handlerExecutions.put(entry.getKey(), entry.getValue()));

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private boolean hasMethodAnnotatedRequestMapping(final Class<?> controller) {
        return Arrays.stream(controller.getMethods())
                .anyMatch(method -> method.isAnnotationPresent(RequestMapping.class));
    }

    private <T> T newInstance(final Class<T> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<Entry<HandlerKey, HandlerExecution>> createRequestMappings(final Object instance) {
        final var clazz = instance.getClass();

        return Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .map(method -> Map.entry(createHandlerKey(method), createHandlerExecution(method, instance)));
    }

    private HandlerKey createHandlerKey(final Method method) {
        final var requestMapping = method.getAnnotation(RequestMapping.class);
        final var url = requestMapping.value();
        final var requestMethod = requestMapping.method()[0];

        return new HandlerKey(url, requestMethod);
    }

    private HandlerExecution createHandlerExecution(final Method method, final Object instance) {
        return new HandlerExecution(method, instance);
    }

    public Object getHandler(final HttpServletRequest request) {
        final var requestURI = request.getRequestURI();
        final var requestMethod = RequestMethod.from(request.getMethod());
        final var handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
