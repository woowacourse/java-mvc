package nextstep.mvc.mapping;

import nextstep.mvc.exception.MvcException;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Object... packages) {
        this(new Reflections(packages));
    }

    public ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllerClasses);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllerClasses) {
        return controllerClasses.stream()
                .collect(Collectors.toMap(Function.identity(), this::createInstance));
    }

    private Object createInstance(Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException exception) {
            throw new MvcException(
                    String.format("Error while creating instance of controller %s for handler mapping process", controllerClass.getSimpleName()),
                    exception
            );
        }
    }
}
