package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().startsWith("test")) {
                System.out.println("Running " + method.getName());
                method.invoke(clazz.getDeclaredConstructor().newInstance());
            }
        }
    }
}
