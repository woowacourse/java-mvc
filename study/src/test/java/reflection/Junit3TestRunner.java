package reflection;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.lang.reflect.Method;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Reflections reflections = new Reflections(clazz);

        for (Method method : reflections.getMethodsAnnotatedWith(Test.class)) {
            method.invoke(clazz);
        }
    }
}
