package nextstep.context;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public enum PeanutContainer {

    INSTANCE;

    private Map<Class<?>, Object> peanuts = new HashMap<>();

    public void init(final String path) {
        final Reflections reflections = new Reflections(path);
        final Set<Class<?>> handlers = reflections.getTypesAnnotatedWith(Controller.class);
        for (final Class<?> handlerClass : handlers) {
            final Object handler = dynamicCreateHandler(handlerClass);
            peanuts.put(handlerClass, handler);
        }
    }

    private Object dynamicCreateHandler(final Class<?> handlerClass) {
        try {
            return handlerClass.getConstructor().newInstance();
        } catch (InstantiationException |
                 IllegalAccessException |
                 InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T getPeanut(final Class<T> clazz) {
        return (T) peanuts.get(clazz);
    }
}
