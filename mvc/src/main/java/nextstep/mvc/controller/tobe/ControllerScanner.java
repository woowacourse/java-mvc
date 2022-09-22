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

    public static Map<HandlerKey, HandlerExecution> scan(final Object... basePackages) {
        return new Reflections(basePackages)
                .getTypesAnnotatedWith(Controller.class)
                .stream()
                .filter(ControllerScanner::hasMethodAnnotatedRequestMapping)
                .map(ControllerScanner::newInstance)
                .flatMap(ControllerScanner::createRequestMappings)
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    private static boolean hasMethodAnnotatedRequestMapping(final Class<?> controller) {
        return Arrays.stream(controller.getMethods())
                .anyMatch(method -> method.isAnnotationPresent(RequestMapping.class));
    }

    private static <T> T newInstance(final Class<T> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static Stream<Entry<HandlerKey, HandlerExecution>> createRequestMappings(final Object instance) {
        final var clazz = instance.getClass();

        return Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .flatMap((method -> createHandlerEntry(instance, method)));
    }

    private static Stream<Entry<HandlerKey, HandlerExecution>> createHandlerEntry(final Object instance,
                                                                                  final Method method) {
        final var handlerKeys = createHandlerKeys(method);
        final var handlerExecution = new HandlerExecution(method, instance);

        return handlerKeys.stream()
                .map(handlerKey -> Map.entry(handlerKey, handlerExecution));
    }

    private static List<HandlerKey> createHandlerKeys(final Method method) {
        final var requestMapping = method.getAnnotation(RequestMapping.class);
        final var urls = requestMapping.value();
        final var requestMethods = requestMapping.method();

        return Arrays.stream(urls)
                .flatMap(combineUrlAndRequestMethods(requestMethods))
                .collect(Collectors.toList());
    }

    private static Function<String, Stream<? extends HandlerKey>> combineUrlAndRequestMethods(
            final RequestMethod[] requestMethods) {
        return url -> Arrays.stream(requestMethods).map(requestMethod -> new HandlerKey(url, requestMethod));
    }

    private ControllerScanner() {
    }
}
