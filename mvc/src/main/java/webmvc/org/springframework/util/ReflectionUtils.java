package webmvc.org.springframework.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
 * Class like core.org.springframework.util.ReflectionUtils
 */
public class ReflectionUtils {

    public static <T> Constructor<T> accessibleConstructor(Class<T> clazz, Class<?>... parameterTypes)
            throws NoSuchMethodException {

        Constructor<T> ctor = clazz.getDeclaredConstructor(parameterTypes);
        makeAccessible(ctor);
        return ctor;
    }

    private static void makeAccessible(Constructor<?> ctor) {
        // TODO: Deprecated 메서드 사용 변경
        if ((!Modifier.isPublic(ctor.getModifiers()) ||
                !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
            ctor.setAccessible(true);
        }
    }
}
