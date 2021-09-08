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

    public Map<Class<?>, Object> scan(String rootPath) {
        Map<Class<?>, Object> container = new HashMap<>();

        Set<Class<?>> components = AnnotationHandleUtils.getClassesAnnotated(rootPath, COMPONENT_ANNOTATIONS);
        components.forEach(component -> registerBean(container, component));

        return container;
    }

    public boolean contains(Map<Class<?>, Object> container, Class<?> type) {
        return container.containsKey(type);
    }

    private Object registerBean(Map<Class<?>, Object> container, Class<?> component) {
        if (container.containsKey(component)) {
            throw new MvcComponentException("빈 타입은 중복될 수 없습니다.");
        }
        Object bean = instanceBean(container, component);
        container.put(component, bean);
        return bean;
    }

    private Object instanceBean(Map<Class<?>, Object> container, Class<?> component) {
        try {
            Constructor<?> constructor = component.getConstructors()[0];
            Object[] parameterBeans = Arrays.stream(constructor.getParameterTypes())
                    .map(parameterType -> injectConstructorParam(container, parameterType))
                    .toArray();
            return constructor.newInstance(parameterBeans);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MvcComponentException("빈 생성 실패");
        }
    }

    private Object injectConstructorParam(Map<Class<?>, Object> container, Class<?> parameterType) {
        if (container.containsKey(parameterType)) {
            return container.get(parameterType);
        }
        return registerBean(container, parameterType);
    }
}
