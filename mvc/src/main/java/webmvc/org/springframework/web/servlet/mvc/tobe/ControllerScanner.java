package webmvc.org.springframework.web.servlet.mvc.tobe;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import context.org.springframework.stereotype.Controller;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);
    private static final String EMPTY = "";

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> types = reflections.getTypesAnnotatedWith(Controller.class);
        return types.stream()
                .collect(toMap(identity(), this::instantiate));
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

    public Map<Class<?>, String> getUriPrefixes() {
        final Set<Class<?>> types = reflections.getTypesAnnotatedWith(Controller.class);
        return types.stream()
                .collect(toMap(identity(), this::parseUri));
    }

    private String parseUri(Class<?> type) {
        if (!type.isAnnotationPresent(RequestMapping.class)) {
            return EMPTY;
        }
        final RequestMapping requestMapping = type.getDeclaredAnnotation(RequestMapping.class);
        return requestMapping.value();
    }
}
