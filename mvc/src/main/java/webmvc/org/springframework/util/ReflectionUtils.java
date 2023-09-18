package webmvc.org.springframework.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/**
 * Class like core.org.springframework.util.ReflectionUtils
 */
public class ReflectionUtils {

    public static <T> Object instantiate(Class<T> clazz) {
        try {
            return ReflectionUtils.accessibleConstructor(clazz).newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new ReflectionUtilsException();
        }
    }

    private static <T> Constructor<T> accessibleConstructor(Class<T> clazz, Class<?>... parameterTypes)
            throws NoSuchMethodException {

        Constructor<T> ctor = clazz.getDeclaredConstructor(parameterTypes);
        makeAccessible(ctor);
        return ctor;
    }

    private static void makeAccessible(Constructor<?> ctor) {
        if ((!Modifier.isPublic(ctor.getModifiers()) ||
                !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
            ctor.setAccessible(true);
        }
    }
}
