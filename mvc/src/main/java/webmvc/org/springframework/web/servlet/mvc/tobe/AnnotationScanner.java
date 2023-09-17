package webmvc.org.springframework.web.servlet.mvc.tobe;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import context.org.springframework.stereotype.Controller;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;

public class AnnotationScanner {

    private static final Logger log = LoggerFactory.getLogger(AnnotationScanner.class);
    private static final String EMPTY = "";

    private final Reflections reflections;

    public AnnotationScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, ControllerInstance> scanControllers() {
        final Set<Class<?>> types = reflections.getTypesAnnotatedWith(Controller.class);
        return types.stream()
                .collect(toMap(identity(), type -> new ControllerInstance(instantiate(type), parseUri(type))));
    }

    private Object instantiate(final Class<?> type) {
        try {
            final Constructor<?> declaredConstructor = type.getDeclaredConstructor();
            return declaredConstructor.newInstance();
        } catch (final Exception e) {
            log.error("Instantiate Failed!", e);
            throw new IllegalArgumentException("Instantiate Failed");
        }
    }

    private String parseUri(Class<?> type) {
        if (!type.isAnnotationPresent(RequestMapping.class)) {
            return EMPTY;
        }
        final RequestMapping requestMapping = type.getDeclaredAnnotation(RequestMapping.class);
        return requestMapping.value();
    }

    public Set<Method> scanHttpMappingMethods(final Set<Class<?>> types) {
        return types.stream()
                .flatMap(type -> Arrays.stream(type.getDeclaredMethods()))
                .filter(this::isHttpMappingAnnotationPresent)
                .collect(toSet());
    }

    private boolean isHttpMappingAnnotationPresent(final Method method) {
        final boolean isHttpMappingAnnotationPresent = Arrays.stream(method.getDeclaredAnnotations())
                .anyMatch(annotation -> annotation.annotationType().isAnnotationPresent(RequestMapping.class));
        return method.isAnnotationPresent(RequestMapping.class) || isHttpMappingAnnotationPresent;
    }
}
