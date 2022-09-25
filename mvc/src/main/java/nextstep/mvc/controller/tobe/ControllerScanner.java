package nextstep.mvc.controller.tobe;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private static final Reflections reflections = new Reflections("samples");

    public static Map<Class<?>, Object> findAllControllers() {
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Controller.class);

        Map<Class<?>, Object> map = new HashMap<>();
        try {
            for (Class<?> annotation : annotated) {
                Constructor<?> declaredConstructor = annotation.getDeclaredConstructor();
                map.put(annotation, declaredConstructor.newInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;

    }
}
