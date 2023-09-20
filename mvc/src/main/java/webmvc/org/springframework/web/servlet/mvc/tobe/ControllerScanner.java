package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(final Object[] basePackage) {
        this(new Reflections(basePackage));
    }

    public ControllerScanner(final Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        return getInstantiateControllers(controllers);
    }

    private Map<Class<?>, Object> getInstantiateControllers(final Set<Class<?>> controllers) {
        return controllers.stream()
                .collect(Collectors.toMap(controller -> controller, this::instantiateController));
    }

    private Object instantiateController(final Class<?> controller) {
        try {
            return controller.getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("해당 컨트롤러를 인스턴스화 할 수 없습니다.");
        }
    }
}
