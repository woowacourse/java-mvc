package nextstep.mvc.controller.tobe;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {
    private final Reflections reflections;

    public ControllerScanner(Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
    }


    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        return InstantiateControllers(controllerClasses);
    }

    private Map<Class<?>, Object> InstantiateControllers(Set<Class<?>> controllerClasses) {
        Map<Class<?>, Object> instantiateControllers = new HashMap<>();
        for (Class<?> controllerClass : controllerClasses) {
            try {
                instantiateControllers.put(controllerClass, controllerClass.getConstructor().newInstance());
            } catch (Exception e) {
                throw new IllegalArgumentException("컨트롤러 인스턴트화 실패");
            }
        }
        return instantiateControllers;
    }
}
