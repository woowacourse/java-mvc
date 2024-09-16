package reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        final Class<Junit4Test> clazz = Junit4Test.class;
        Stream.of(clazz.getMethods())
                .filter(this::hasAnnotation)
                .forEach(method -> invokeMethod(clazz,method));
    }

    private boolean hasAnnotation(final Method method) {
        return method.isAnnotationPresent(MyTest.class);
    }

    private void invokeMethod(final Class<?> clazz, final Method method) {
        try {
            final Object instance = clazz.getDeclaredConstructor().newInstance();
            method.invoke(instance);
        } catch (final NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
