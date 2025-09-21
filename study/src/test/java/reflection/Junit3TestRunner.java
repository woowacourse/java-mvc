package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Junit3Test junit4Test = clazz.getDeclaredConstructor().newInstance();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().startsWith("test")) {
                System.out.println("Invoking: " + method.getName());
                method.invoke(junit4Test);
            }
        }
    }
}
