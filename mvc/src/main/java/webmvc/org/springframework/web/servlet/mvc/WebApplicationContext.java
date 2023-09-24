package webmvc.org.springframework.web.servlet.mvc;

import context.org.springframework.stereotype.Controller;
import core.org.springframework.util.ReflectionUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

public class WebApplicationContext {

    private static final String INTERNAL_BASE_PACKAGE = "webmvc.org.springframework.web.servlet.mvc";
    private static final List<Class<? extends Annotation>> COMPONENT_CLASS_ANNOTATIONS = java.util.List.of(Controller.class);
    private static final List<Class<?>> WEB_APPLICATION_DEFAULT_SUPER_CLASSES = java.util.List.of(
            HandlerMapping.class,
            HandlerAdapter.class
    );

    private final Set<Object> beans;

    public static WebApplicationContext createContextForPackage(final Object... externalBasePackages) {
        final var componentAnnotatedClassesInPackage = ClassPathScanner.getAllAnnotatedClassesInPackage(COMPONENT_CLASS_ANNOTATIONS, externalBasePackages, INTERNAL_BASE_PACKAGE);
        final var mvcDefaultClassesInPackage = ClassPathScanner.getAllConcreteClassesInPackage(WEB_APPLICATION_DEFAULT_SUPER_CLASSES, INTERNAL_BASE_PACKAGE, externalBasePackages);
        return new WebApplicationContext(
                Stream.concat(
                              componentAnnotatedClassesInPackage.stream(),
                              mvcDefaultClassesInPackage.stream()
                      )
                      .collect(Collectors.toSet())
        );
    }

    public WebApplicationContext(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        this.beans.forEach(this::setFields);
    }

    private Set<Object> createBeans(final Set<Class<?>> classes) {
        return classes.stream()
                      .map(ReflectionUtils::instantiate)
                      .collect(Collectors.toSet());
    }

    private void setFields(final Object o) {
        final var clazz = o.getClass();
        setInjectAnnotatedFields(o, clazz);
        setApplicationContexts(o, clazz);
    }

    private void setApplicationContexts(final Object o, final Class<?> clazz) {
        Arrays.stream(clazz.getDeclaredFields())
              .filter(field -> field.getType().equals(WebApplicationContext.class))
              .forEach(field -> setApplicationContext(o, field));
    }

    private void setApplicationContext(final Object o, final Field field) {
        field.setAccessible(true);
        try {
            field.set(o, this);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void setInjectAnnotatedFields(final Object o, final Class<?> clazz) {
        Arrays.stream(clazz.getDeclaredFields())
              .filter(field -> field.isAnnotationPresent(Inject.class))
              .forEach(field -> setFieldByBean(o, field));
    }

    private void setFieldByBean(final Object o, final Field field) {
        field.setAccessible(true);
        try {
            field.set(o, getBean(field.getType()));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        final Object o = beans.stream()
                              .filter(aClass::isInstance)
                              .findFirst()
                              .orElseThrow(() -> new IllegalArgumentException("해당하는 클래스가 존재하지 않습니다."));
        return (T) o;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getBeansBySuperType(final Class<T> superType) {
        return beans.stream()
                    .filter(superType::isInstance)
                    .map(o -> (T) o)
                    .collect(Collectors.toList());
    }

    public List<Object> getBeansAnnotatedWith(final Class<? extends Annotation> annotation) {
        return beans.stream()
                    .filter(bean -> bean.getClass().isAnnotationPresent(annotation))
                    .collect(Collectors.toList());
    }
}
