package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    public Map<HandlerKey, HandlerExecution> scan() {
        return this.reflections
                .getTypesAnnotatedWith(Controller.class)
                .stream()
                .filter(this::hasMethodAnnotatedRequestMapping)
                .map(this::newInstance)
                .flatMap(this::createRequestMappings)
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
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
                .flatMap((method -> createHandlerEntry(instance, method)));
    }

    private Stream<Entry<HandlerKey, HandlerExecution>> createHandlerEntry(final Object instance,
                                                                           final Method method) {
        final var handlerKeys = createHandlerKeys(method);
        final var handlerExecution = new HandlerExecution(method, instance);

        return handlerKeys.stream()
                .map(handlerKey -> Map.entry(handlerKey, handlerExecution));
    }

    private List<HandlerKey> createHandlerKeys(final Method method) {
        final var requestMapping = method.getAnnotation(RequestMapping.class);
        final var urls = requestMapping.value();
        final var requestMethods = requestMapping.method();

        return Arrays.stream(urls)
                .flatMap(combineUrlAndRequestMethods(requestMethods))
                .collect(Collectors.toList());
    }

    private Function<String, Stream<? extends HandlerKey>> combineUrlAndRequestMethods(
            final RequestMethod[] requestMethods) {
        return url -> Arrays.stream(requestMethods).map(requestMethod -> new HandlerKey(url, requestMethod));
    }
}
