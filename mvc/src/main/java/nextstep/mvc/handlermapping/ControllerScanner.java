package nextstep.mvc.handlermapping;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Object[] basePackages;

    public ControllerScanner(Object... basePackages) {
        this.basePackages = basePackages;
    }

    public List<Object> getControllers() {
        Set<Class<?>> controllerClasses = getControllerClasses(basePackages);

        return controllerClasses.stream()
                .map(this::createController)
                .collect(Collectors.toList());
    }

    private Set<Class<?>> getControllerClasses(Object[] basePackages) {
        Reflections reflections = new Reflections(basePackages);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private Object createController(Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
