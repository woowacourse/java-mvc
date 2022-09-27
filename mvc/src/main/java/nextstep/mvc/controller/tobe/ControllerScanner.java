package nextstep.mvc.controller.tobe;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    public static Map<Class<?>, Object> findAllControllers(Object path) {
        Reflections reflections = new Reflections(path);
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
