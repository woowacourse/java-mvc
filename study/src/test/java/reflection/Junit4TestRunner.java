package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        final Method[] methods = clazz.getDeclaredMethods();
        final Constructor<Junit4Test> constructor = clazz.getConstructor();
        final Junit4Test junit4Test = constructor.newInstance();

        for (final Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MyTest.class)) {
                method.invoke(junit4Test);
            }
        }
    }
}
