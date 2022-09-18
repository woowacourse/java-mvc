package nextstep.mvc.controller.tobe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.web.annotation.Controller;

/**
 * Controller 클래스를 추상화합니다.
 */
public class ControllerClass {

    private final Class<?> clazz;

    private ControllerClass(final Class<?> clazz) {
        this.clazz = clazz;
    }

    public static List<ControllerClass> from(final ControllerPackage controllerPackage) {
        return controllerPackage.getClassNames()
                .stream()
                .map(ControllerClass::nameToClass)
                .filter(ControllerClass::isController)
                .map(ControllerClass::new)
                .collect(Collectors.toList());
    }

    private static Class<?> nameToClass(final String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("경로에 해당하는 클래스를 찾을 수 없습니다.");
            // TODO: 적절한 예외로 변경
        }
    }

    private static boolean isController(final Class<?> it) {
        return it.getAnnotation(Controller.class) != null;
    }

    public Object newInstance() {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new IllegalArgumentException();
            // TODO: 적절한 예외로 변경
        }
    }

    public List<Method> getDeclaredMethods() {
        return List.of(clazz.getDeclaredMethods());
    }
}
