package core.org.springframework.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public abstract class ReflectionUtils {

    /**
     * Obtain an accessible constructor for the given class and parameters.
     *
     * @param clazz          the clazz to check
     * @param parameterTypes the parameter types of the desired constructor
     * @return the constructor reference
     * @throws NoSuchMethodException if no such constructor exists
     * @since 5.0
     */
    public static <T> Constructor<T> accessibleConstructor(Class<T> clazz,
        Class<?>... parameterTypes)
        throws NoSuchMethodException {

        Constructor<T> ctor = clazz.getDeclaredConstructor(parameterTypes);
        makeAccessible(ctor);
        return ctor;
    }

    /**
     * Make the given constructor accessible, explicitly setting it accessible if necessary. The
     * {@code setAccessible(true)} method is only called when actually necessary, to avoid
     * unnecessary conflicts.
     *
     * @param ctor the constructor to make accessible
     * @see Constructor#setAccessible
     */
    @SuppressWarnings("deprecation")
    public static void makeAccessible(Constructor<?> ctor) {
        if ((!Modifier.isPublic(ctor.getModifiers()) ||
            !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
            ctor.setAccessible(true);
        }
    }

    public static Set<Class<?>> getTypesAnnotatedWith(Object[] basePackage,
        Class<? extends Annotation> annotation) {
        return new Reflections(basePackage).getTypesAnnotatedWith(annotation);
    }

    public static List<Method> getMethodsAnnotatedWith(Class<?> type, Class<? extends Annotation> annotation) {
        return Arrays.stream(type.getDeclaredMethods())
                     .filter(method -> method.isAnnotationPresent(annotation))
                     .collect(Collectors.toList());
    }
}
