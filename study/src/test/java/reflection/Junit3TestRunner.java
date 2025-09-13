package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test instance = clazz.getDeclaredConstructor()
                .newInstance();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName()
                    .startsWith("test")
                    && method.getParameterCount() == 0
                    && method.getReturnType() == void.class) {
                method.invoke(instance);
            }
        }
    }
}
