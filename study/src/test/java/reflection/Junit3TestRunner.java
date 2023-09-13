package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        final Constructor<?> constructor = clazz.getDeclaredConstructor();
        final Object junit3Test = constructor.newInstance();

        for (final Method method : clazz.getDeclaredMethods()) {
            method.invoke(junit3Test);
        }
    }
}
