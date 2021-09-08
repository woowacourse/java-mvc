package nextstep.mvc.assembler;

import nextstep.mvc.assembler.annotation.Component;
import nextstep.mvc.exception.MvcComponentException;
import nextstep.mvc.support.annotation.AnnotationHandleUtils;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ComponentScanner {
    private final Map<Class<?>, Object> container = new HashMap<>();

    public void scanFromComponentScan() {
    }

    public void scanFromComponentScan(String rootPath) {
        Set<Class<?>> components = AnnotationHandleUtils.getClassesAnnotated(rootPath, Component.class);
        for (Class<?> component : components) {
            if (container.containsKey(component)) {
                throw new MvcComponentException("빈 타입은 중복될 수 없습니다.");
            }
            registerBean(component);
        }
    }

    private Object registerBean(Class<?> component) {
        Object bean = instanceWithInjection(component);
        container.put(component, bean);
        return bean;
    }

    private Object instanceWithInjection(Class<?> component) {
        try {
//            System.out.println(component.getConstructors().length);
//            Constructor<?> constructor1 = component.getConstructors()[0];
//            System.out.println(constructor1.getParameterCount());

            Constructor<?> constructor = component.getConstructors()[0];
            Object[] parameterBeans = Arrays.stream(constructor.getParameterTypes())
                    .map(this::getBean)
                    .toArray();
            return constructor.newInstance(parameterBeans);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MvcComponentException("빈 타입은 중복될 수 없습니다.");
        }
    }

    private Object getBean(Class<?> type) {
        if (container.containsKey(type)) {
            return container.get(type);
        }
        return registerBean(type);
    }

    public boolean contains(Class<?> type) {
        return container.containsKey(type);
    }
}
