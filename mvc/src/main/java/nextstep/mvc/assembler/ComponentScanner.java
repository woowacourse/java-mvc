package nextstep.mvc.assembler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.assembler.annotation.Component;
import nextstep.mvc.assembler.annotation.ComponentScan;
import nextstep.mvc.exception.MvcComponentException;
import nextstep.mvc.support.annotation.AnnotationHandleUtils;
import nextstep.web.annotation.Controller;

public class ComponentScanner {

    private static final Class<? extends Annotation>[] COMPONENT_ANNOTATIONS
            = new Class[]{Component.class, ComponentScan.class, Controller.class};

    private final Map<Class<?>, Object> container = new HashMap<>();

    public ComponentScanner(String rootPath) {
        Set<Class<?>> components = AnnotationHandleUtils.getClassesAnnotated(rootPath, COMPONENT_ANNOTATIONS);
        components.forEach(this::registerBean);
    }

    private Object registerBean(Class<?> component) {
        if (container.containsKey(component)) {
            throw new MvcComponentException("빈 타입은 중복될 수 없습니다.");
        }

        Object bean = instanceWithInjection(component);
        container.put(component, bean);
        return bean;
    }

    private Object instanceWithInjection(Class<?> component) {
        try {
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
