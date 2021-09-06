package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    final Map<Class<?>, Object> controllers = new HashMap<>();

    public ControllerScanner()
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        init();
    }

    private void init()
        throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Reflections reflections = new Reflections("com.techcourse.controller");
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> aClass : controllers) {
            this.controllers.put(aClass, aClass.getDeclaredConstructor().newInstance());
        }
    }

    public Map<Class<?>, Object> getControllers() {
        return new HashMap<>(controllers);
    }
}
