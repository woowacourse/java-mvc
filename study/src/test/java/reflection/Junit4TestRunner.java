package reflection;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        var instance = clazz.getDeclaredConstructor().newInstance();
        var methods = clazz.getMethods();
        var myTestMethods = Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .toList();

        for (var method : myTestMethods) {
            method.invoke(instance);
        }
    }
}
