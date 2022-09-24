package nextstep.mvc.controller.tobe;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.controller.exception.NoConstructorException;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {
    public static Map<Class<?>, Object> scan(Object... basePackages) {
        final Map<Class<?>, Object> handlers = new HashMap<>();
        Set<Class<?>> handlerClasses = getHandlerClasses(basePackages);

        for (Class<?> handlerClass : handlerClasses) {
            handlers.put(handlerClass, newInstanceOf(handlerClass));
        }
        return handlers;
    }

    private static Set<Class<?>> getHandlerClasses(Object... basePackages) {
        final Reflections reflections = new Reflections(basePackages);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private static Object newInstanceOf(Class<?> handlerClass) {
        try {
            final Constructor<?> constructor = handlerClass.getConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            throw new NoConstructorException();
        }
    }
}
