package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4Test = clazz.getDeclaredConstructor().newInstance();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        Arrays.stream(declaredMethods)
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .forEach(method -> invoke(method, junit4Test));
    }

    private void invoke(Method method, Junit4Test junit4Test) {
        try {
            method.invoke(junit4Test);
        } catch (Exception e) {
            throw new RuntimeException(e);
        };
    }
}
