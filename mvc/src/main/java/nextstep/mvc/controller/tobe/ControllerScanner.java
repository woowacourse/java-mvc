package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

    public Map<Class<?>, Object> scan(Object[] basePackage) {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        return controllerClasses.stream()
                .collect(Collectors.toMap(controller -> controller, this::createControllerObject));
    }

    private Object createControllerObject(final Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException(controllerClass.getName() + "의 컨트롤러 인스턴스를 생성할 수 없습니다.", e);
        }
    }
}
