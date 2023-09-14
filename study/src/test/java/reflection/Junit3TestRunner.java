package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        final Junit3Test test = new Junit3Test();
        final Method[] methods = test.getClass().getDeclaredMethods();

        for (final Method method : methods) {
            if (method.getName().startsWith("test")) {
                method.invoke(test);
            }
        }
    }
}
