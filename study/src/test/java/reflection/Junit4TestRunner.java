package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        List<Method> testMethods = Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .toList();

        for (Method testMethod : testMethods) {
            testMethod.invoke(new Junit4Test());
        }
    }
}
