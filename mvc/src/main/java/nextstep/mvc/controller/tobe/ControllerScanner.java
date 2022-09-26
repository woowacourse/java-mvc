package nextstep.mvc.controller.tobe;

import static org.reflections.Reflections.*;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;

import nextstep.web.annotation.Controller;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllerMapping() {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        return controllers.stream()
            .collect(Collectors.toMap(it -> it, this::instantiateController));
    }

    private Object instantiateController(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new RuntimeException("fail to instantiate controller");
        }
    }
}
