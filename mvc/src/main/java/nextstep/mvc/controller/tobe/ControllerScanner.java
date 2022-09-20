package nextstep.mvc.controller.tobe;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
}
