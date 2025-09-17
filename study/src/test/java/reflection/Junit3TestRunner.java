package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        final var junit3Test = clazz.getDeclaredConstructor().newInstance();
        final var declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            final var name = method.getName();
            if (name.startsWith("test")) {
                method.invoke(junit3Test);
            }
        }
    }
}
