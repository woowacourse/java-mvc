package webmvc.org.springframework.web.servlet.mvc;

import org.reflections.Reflections;
import webmvc.org.springframework.web.servlet.mvc.exception.ComponentScaneException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ComponentScanner {

    private final Reflections reflections;

    public ComponentScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Object, Method> getMethodsAnnotateWithFromTypes(
            Class<? extends Annotation> typeAnnotation,
            Class<? extends Annotation> methodAnnotation
    ) {
        return getTypesAnnotateWith(typeAnnotation).stream()
                .map(clazz -> getMethodsAnnotateWith(clazz, methodAnnotation))
                .flatMap(Set::stream)
                .collect(Collectors.toMap(
                        method -> createInstance(method.getDeclaringClass()),
                        method -> method)
                );
    }

    public Optional<Class<?>> getSingleTypeAnnotateWith(Class<? extends Annotation> annotation) {
        final var classes = reflections.getTypesAnnotatedWith(annotation);
        if (classes.size() > 1) {
            throw new ComponentScaneException("Controller Advice should be one");
        }

        return Optional.ofNullable(classes.iterator().next());
    }

    public Set<Class<?>> getTypesAnnotateWith(Class<? extends Annotation> annotation) {
        return reflections.getTypesAnnotatedWith(annotation);
    }

    private Set<Method> getMethodsAnnotateWith(Class<?> clazz, Class<? extends Annotation> annotation) {
        return Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(annotation))
                .collect(Collectors.toSet());
    }

    public Object createInstance(Class<?> clazz) {
        try {
            final var constructor = clazz.getConstructor();

            return createInstance(constructor);
        } catch (NoSuchMethodException exception) {
            throw new ComponentScaneException("Class does not have a default constructor;", exception);
        }
    }

    private Object createInstance(Constructor<?> constructor) {
        try {
            return constructor.newInstance();
        } catch (InvocationTargetException exception) {
            throw new ComponentScaneException("Cannot invoke a constructor;", exception);
        } catch (InstantiationException exception) {
            throw new ComponentScaneException("Cannot instantiate an abstract class or interface;", exception);
        } catch (IllegalAccessException exception) {
            throw new ComponentScaneException("Cannot access to a constructor;", exception);
        }
    }

}
