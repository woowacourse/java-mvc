package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        var junit3Test = clazz.getDeclaredConstructor().newInstance();
        var methods = clazz.getDeclaredMethods();
        var testMethods = Arrays.stream(methods)
                .filter(method -> method.getName().startsWith("test"))
                .toList();

        for (Method method : testMethods) {
            method.invoke(junit3Test);
        }
    }
}
