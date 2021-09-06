package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        final Class<Junit3Test> clazz = Junit3Test.class;
        final Junit3Test test = clazz.getConstructor().newInstance();

        for (Method method : clazz.getMethods()) {
            final String methodName = method.getName();
            if (methodName.startsWith("test")) {
                method.invoke(test);
            }
        }
    }
}
