package nextstep.mvc.controller.tobe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.mvc.controller.tobe.exception.ClassCreationException;
import nextstep.mvc.controller.tobe.exception.ClassInstantiateException;
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
            throw new ClassCreationException();
        }
    }

    private static boolean isController(final Class<?> it) {
        return it.getAnnotation(Controller.class) != null;
    }

    public Object newInstance() {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new ClassInstantiateException();
        }
    }

    public List<Method> getDeclaredMethods() {
        return List.of(clazz.getDeclaredMethods());
    }
}
