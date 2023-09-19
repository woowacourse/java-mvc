package core.org.springframework.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

public class ReflectionUtils {

    /**
     * Create a new instance of the specified {@code clazz} by invoking its default constructor.
     * @param clazz the clazz to instantiate
     * @return the new instance
     * @param <T> the type of the class
     */
    public static <T> T instantiate(final Class<T> clazz) {
        try {
            return ReflectionUtils.accessibleConstructor(clazz).newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtain an accessible constructor for the given class and parameters.
     * @param clazz the clazz to check
     * @param parameterTypes the parameter types of the desired constructor
     * @return the constructor reference
     * @throws NoSuchMethodException if no such constructor exists
     * @since 5.0
     */
    public static <T> Constructor<T> accessibleConstructor(final Class<T> clazz, Class<?>... parameterTypes)
            throws NoSuchMethodException {

        Constructor<T> ctor = clazz.getDeclaredConstructor(parameterTypes);
        makeAccessible(ctor);
        return ctor;
    }

    /**
     * Make the given constructor accessible, explicitly setting it accessible
     * if necessary. The {@code setAccessible(true)} method is only called
     * when actually necessary, to avoid unnecessary conflicts.
     * @param ctor the constructor to make accessible
     * @see Constructor#setAccessible
     */
    @SuppressWarnings("deprecation")
    public static void makeAccessible(final Constructor<?> ctor) {
        if ((!Modifier.isPublic(ctor.getModifiers()) ||
                !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
            ctor.setAccessible(true);
        }
    }
}
