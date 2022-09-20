package nextstep.mvc.controller.asis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.controller.exception.CreateObjectException;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import org.reflections.Reflections;

public class ControllerScanner {

    public static Set<Class<?>> findAnnotatedController(final Object[] basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    public static List<Method> findAnnotatedMethod(final Class<?> handlerClassFile) {
        return Arrays.stream(handlerClassFile.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    public static Object newInstance(final Class<?> handlerClassFile) {
        try {
            return handlerClassFile
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new CreateObjectException();
        }
    }
}
