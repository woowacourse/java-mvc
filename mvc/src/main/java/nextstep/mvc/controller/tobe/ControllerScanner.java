package nextstep.mvc.controller.tobe;

import nextstep.mvc.exception.MvcException;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> scanControllers() {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(controllers);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> classes) {
        return classes.stream()
                .collect(
                        Collectors.toMap(Function.identity(), this::instantiate)
                );
    }

    private Object instantiate(Class<?> aClass) {
        try {
            return aClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new MvcException(String.format("%s 객체 생성에 실패했습니다.", aClass.getSimpleName()));
        }
    }
}
