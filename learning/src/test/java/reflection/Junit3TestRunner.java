package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Constructor<Junit3Test> constructor = clazz.getDeclaredConstructor();
        Junit3Test junit3Test = constructor.newInstance();

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            method.invoke(junit3Test);
        }
    }
}
