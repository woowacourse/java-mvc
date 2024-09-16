package reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() {
        final Class<Junit3Test> clazz = Junit3Test.class;
        Stream.of(clazz.getMethods())
                .filter(this::hasPrefix)
                .forEach(method -> invokeMethod(clazz, method));
    }

    private boolean hasPrefix(final Method method) {
        return method.getName().startsWith("test");
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
