package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        final Class<Junit3Test> clazz = Junit3Test.class;

        final Method[] methods = clazz.getDeclaredMethods();
        for (final Method method : methods) {
            if (method.getName().startsWith("test")) {
                final Junit3Test junit3Test = clazz.getDeclaredConstructor().newInstance();
                method.invoke(junit3Test);
            }
        }
    }
}
