package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final Map<Class<?>, Object> controllers = new HashMap<>();
    private final String packagePath;

    public ControllerScanner(String packagePath)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        this.packagePath = packagePath;
        init();
    }

    private void init()
        throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Reflections reflections = new Reflections(packagePath);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> aClass : controllers) {
            this.controllers.put(aClass, aClass.getDeclaredConstructor().newInstance());
        }
    }

    public Map<Class<?>, Object> getControllers() {
        return new HashMap<>(controllers);
    }
}
