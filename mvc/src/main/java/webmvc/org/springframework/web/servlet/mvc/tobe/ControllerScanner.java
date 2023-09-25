package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ControllerScanner {
    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> typesAnnotatedWith = this.reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(typesAnnotatedWith);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllers) {
        return controllers.stream()
                .collect(Collectors.toMap(controller -> controller, this::instantiateController));
    }

    private Object instantiateController(final Class<?> controller) {
        try {
            return controller.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
