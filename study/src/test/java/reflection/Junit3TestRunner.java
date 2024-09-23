package reflection;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        for (Method method : clazz.getMethods()) {
            if (method.getName().startsWith("test")) {
                method.invoke(clazz.getDeclaredConstructor().newInstance());
            }
        }
    }
}
