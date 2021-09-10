package nextstep.core;

import java.lang.annotation.Annotation;

public class BeanDefinition {

    private final Class<?> clazz;
    private final Object bean;

    public BeanDefinition(Class<?> clazz, Object bean) {
        this.clazz = clazz;
        this.bean = bean;
    }

    public <U> boolean isTypeOf(Class<U> targetClass) {
        return targetClass.isAssignableFrom(clazz);
    }

    public Object getTarget() {
        return bean;
    }

    public boolean hasAnnotation(Class<? extends Annotation> annotation) {
        return clazz.isAnnotationPresent(annotation);
    }

    public Class<?> getTargetClass() {
        return clazz;
    }

    public String getBeanName() {
        return clazz.getName();
    }
}
