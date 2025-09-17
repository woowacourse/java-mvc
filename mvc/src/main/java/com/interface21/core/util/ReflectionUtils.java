package com.interface21.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public abstract class ReflectionUtils {

    public static <T> Constructor<T> accessibleConstructor(Class<T> clazz, Class<?>... parameterTypes)
            throws NoSuchMethodException {

        Constructor<T> ctor = clazz.getDeclaredConstructor(parameterTypes);
        makeAccessible(ctor);
        return ctor;
    }

    @SuppressWarnings("deprecation")
    public static void makeAccessible(Constructor<?> ctor) {
        if ((!Modifier.isPublic(ctor.getModifiers()) ||
                !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
            ctor.setAccessible(true);
        }
    }

    public static Set<Method> getAllMethods(Class<?> clazz, Predicate<Method> methodFilter) {
        final Set<Method> methods = new HashSet<>();

        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            for (final Method method : currentClass.getDeclaredMethods()) {
                if (methodFilter.test(method)) {
                    methods.add(method);
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        return methods;
    }

    public static Predicate<Method> withAnnotation(Class<? extends Annotation> annotationType) {
        return method -> method.isAnnotationPresent(annotationType);
    }
}
