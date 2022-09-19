package nextstep.mvc.controller.tobe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private static final Reflections reflections = new Reflections("samples");

    public static Map<Class<?>, Object> findAllControllers()
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Controller.class);
        annotated.forEach(System.out::println);

        Map<Class<?>, Object> map = new HashMap<>();
        for (Class<?> annotation : annotated) {
            Constructor<?> declaredConstructor = annotation.getDeclaredConstructor();
            declaredConstructor.newInstance();
            map.put(annotation, declaredConstructor.newInstance());
        }
        return map;
    }
}
