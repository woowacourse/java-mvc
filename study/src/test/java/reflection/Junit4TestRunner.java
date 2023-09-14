package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        final Junit4Test test = new Junit4Test();
        final Method[] methods = test.getClass().getDeclaredMethods();

        for (final Method method : methods) {
            if (method.isAnnotationPresent(MyTest.class)) {
                method.invoke(test);
            }
        }
    }
}
