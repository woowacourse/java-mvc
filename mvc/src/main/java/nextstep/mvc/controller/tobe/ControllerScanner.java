package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    private final String basePackage;

    public ControllerScanner(final String basePackage) {
        this.basePackage = basePackage;
    }

    public Map<Class<?>, Object> getControllers() {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> clazzWithController = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(clazzWithController);
    }

    public Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> clazzWithController) {
        Map<Class<?>, Object> controllers = new HashMap<>();

        for (Class<?> clazz : clazzWithController) {
            try {
                final Object object = clazz.getDeclaredConstructor().newInstance();
                controllers.put(clazz, object);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException("해당하는 클래스 정보로 인스턴스를 생성할 수 없습니다.");
            }
        }
        return controllers;
    }
}
