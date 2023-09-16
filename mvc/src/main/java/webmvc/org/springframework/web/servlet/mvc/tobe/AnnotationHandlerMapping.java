package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
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

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackage = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        final var reflections = new Reflections(basePackage);
        final var methodsPerClass = groupingMethodsByClass(reflections);

        for (final var classAndMethods : methodsPerClass.entrySet()) {
            final var clazz = classAndMethods.getKey();
            final var methods = classAndMethods.getValue();
            try {
                initializeEachClassExecutions(clazz, methods);
            } catch (final InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Map<? extends Class<?>, List<Method>> groupingMethodsByClass(final Reflections reflections) {
        return reflections.getTypesAnnotatedWith(Controller.class).stream()
                          .map(Class::getDeclaredMethods)
                          .flatMap(Stream::of)
                          .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                          .collect(Collectors.groupingBy(Method::getDeclaringClass));
    }

    private void initializeEachClassExecutions(
            final Class<?> clazz,
            final List<Method> methods
    ) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final var parentPath = clazz.getAnnotation(Controller.class).path();
        final var target = clazz.getConstructor().newInstance();
        for (var method : methods) {
            initializeEachMethodExecution(method, parentPath, target);
        }
    }

    private void initializeEachMethodExecution(final Method method, final String parentPath, final Object target) {
        final var requestMapping = method.getAnnotation(RequestMapping.class);
        final var path = parentPath + requestMapping.value();
        Arrays.stream(requestMapping.method())
              .map(requestMethod -> new HandlerKey(path, requestMethod))
              .forEach(key -> handlerExecutions.put(key, new HandlerExecution(target, method)));
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod().toUpperCase())));
    }
}
