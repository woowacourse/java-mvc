package com.interface21.webmvc.servlet.mvc.tobe.annotation;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

/**
 * 컨트롤러 애노테이션이 붙은 클래스를 찾아, 클래스와 인스턴스를 매핑하는 책임을 갖는다.
 */
public class ControllerScanner {
    private final Reflections reflections;

    public ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(classes);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> classes) {
        final Map<Class<?>, Object> instances = new HashMap<>();
        for (Class<?> clazz : classes) {
            try {
                final Constructor<?> constructor = ReflectionUtils.accessibleConstructor(clazz);
                final Object controller = constructor.newInstance();
                instances.put(clazz, controller);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("컨트롤러 생성자 생성 실패: " + clazz.getName(), e.getCause());
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("컨트롤러 생성 실패: " + clazz.getName(), e.getCause());
            }
        }
        return instances;
    }
}
