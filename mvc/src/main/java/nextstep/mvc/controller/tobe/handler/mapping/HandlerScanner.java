package nextstep.mvc.controller.tobe.handler.mapping;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.exception.controller.IllegalNewInstanceException;
import nextstep.mvc.exception.controller.NoSuchConstructorException;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class HandlerScanner {

    private final Map<Class<?>, Object> handlers;

    public HandlerScanner() {
        this(new HashMap<>());
    }

    private HandlerScanner(Map<Class<?>, Object> handlers) {
        this.handlers = handlers;
    }

    public void scan(Object[] basePackages) {
        Reflections reflections = new Reflections(basePackages);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        initializeWith(controllers);
    }

    private void initializeWith(Set<Class<?>> controllers) {
        for (Class<?> controller : controllers) {
            initializeWithEach(controller);
        }
    }

    private void initializeWithEach(Class<?> controller) {
        try {
            handlers.put(controller, getConstructor(controller).newInstance());
        } catch (Exception e) {
            throw new IllegalNewInstanceException();
        }
    }

    private Constructor<?> getConstructor(Class<?> controller) {
        try {
            return controller.getConstructor();
        } catch (Exception e) {
            throw new NoSuchConstructorException();
        }
    }

    public Map<Class<?>, Object> getHandlers() {
        return handlers;
    }
}
