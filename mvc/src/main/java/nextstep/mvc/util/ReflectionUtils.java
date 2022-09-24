package nextstep.mvc.util;

public class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static Object createNewInstance(Class<?> clazz) throws Exception {
        return clazz.getDeclaredConstructor()
                .newInstance();
    }
}
