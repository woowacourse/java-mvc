package com.interface21.core.util;

import java.lang.reflect.Method;
import java.util.Optional;

public abstract class ReflectionMethodUtils {
    public static <T> Optional<Method> getMethodWithOptional(final Class<T> clazz, final String name) {
        try {
            return Optional.of(clazz.getMethod(name));
        } catch (final NoSuchMethodException | NullPointerException e) {
            return Optional.empty();
        }
    }

    private ReflectionMethodUtils() {}
}
