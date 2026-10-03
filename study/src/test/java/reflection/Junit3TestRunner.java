package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test junit3TestInstance = clazz.getDeclaredConstructor().newInstance();

        MethodNameCondition testMethodCondition = new MethodNameCondition("test");

        for (Method method : clazz.getDeclaredMethods()) {
            if (testMethodCondition.included(method)) {
                method.invoke(junit3TestInstance);
            }
        }
    }
}
