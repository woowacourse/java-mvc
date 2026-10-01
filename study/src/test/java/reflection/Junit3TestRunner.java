package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test junit3TestInstance = new Junit3Test();
        MethodNameCondition testMethodCondition = new MethodNameCondition("test");

        Method[] methods = clazz.getDeclaredMethods();
        Arrays.sort(methods, (left, right) -> left.getName().compareTo(right.getName()));

        for (Method method : methods) {
            if (testMethodCondition.included(method)) {
                method.invoke(junit3TestInstance);
            }
        }
    }
}
