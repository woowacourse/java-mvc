package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ControllerScanner {
    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final Reflections reflections;

    public ControllerScanner(final Object[] basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateClasses(classes);
    }

    private Map<Class<?>, Object> instantiateClasses(final Set<Class<?>> classes) {
        return classes.stream()
                .collect(Collectors.toMap(Function.identity(), this::instanticateClazz));
    }

    private Object instanticateClazz(final Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException e) {
            log.error("{} 클래스는 추상클래스이거나 인터페이스입니다.", clazz.getSimpleName());
        } catch (IllegalAccessException e) {
            log.error("{} 클래스의 생성자에 접근할 수 없습니다.", clazz.getSimpleName());
        } catch (InvocationTargetException e) {
            log.error("{} 클래스를 생성할 때 예외가 발생하였습니다.", clazz.getSimpleName());
            log.error("TargetException: {}", e.getTargetException().getMessage());
        } catch (NoSuchMethodException e) {
            log.error("{} 클래스의 기본 생성자를 찾을 수 없습니다.", clazz.getSimpleName());
        }
        throw new IllegalArgumentException();
    }
}
